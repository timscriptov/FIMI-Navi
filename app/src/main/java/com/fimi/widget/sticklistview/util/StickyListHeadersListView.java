package com.fimi.widget.sticklistview.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class StickyListHeadersListView extends ListView {
    private final Rect mClippingRect;
    private final AdapterWrapper.OnHeaderClickListener mAdapterHeaderClickListener;
    private final DataSetObserver mDataSetChangedObserver;
    private final ViewConfiguration mViewConfig;
    public OnLoadingMoreLinstener loadMoreListener;
    public AdapterWrapper mAdapter;
    public AbsListView.OnScrollListener mOnScrollListener;
    public AbsListView.OnScrollListener mOnScrollListenerDelegate;
    private boolean mAreHeadersSticky;
    private Boolean mClippingToPadding;
    private Long mCurrentHeaderId;
    private Drawable mDivider;
    private int mDividerHeight;
    private boolean mDrawingListUnderStickyHeader;
    private ArrayList<View> mFooterViews;
    private View mHeader;
    private boolean mHeaderBeingPressed;
    private int mHeaderBottomPosition;
    private float mHeaderDownY;
    private Integer mHeaderPosition;
    private OnHeaderClickListener mOnHeaderClickListener;
    private Field mSelectorPositionField;
    private Rect mSelectorRect;

    public StickyListHeadersListView(Context context) {
        this(context, null);
    }

    public StickyListHeadersListView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842868);
    }

    @SuppressLint("DiscouragedPrivateApi")
    public StickyListHeadersListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAreHeadersSticky = true;
        this.mClippingRect = new Rect();
        this.mCurrentHeaderId = null;
        this.mHeaderDownY = -1.0f;
        this.mHeaderBeingPressed = false;
        this.mDrawingListUnderStickyHeader = false;
        this.mSelectorRect = new Rect();
        this.mAdapterHeaderClickListener = (header, itemPosition, headerId) -> {
            if (StickyListHeadersListView.this.mOnHeaderClickListener != null) {
                StickyListHeadersListView.this.mOnHeaderClickListener.onHeaderClick(StickyListHeadersListView.this, header, itemPosition, headerId, false);
            }
        };
        this.mDataSetChangedObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                StickyListHeadersListView.this.reset();
            }

            @Override
            public void onInvalidated() {
                StickyListHeadersListView.this.reset();
            }
        };
        this.mOnScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (StickyListHeadersListView.this.mOnScrollListenerDelegate != null) {
                    StickyListHeadersListView.this.mOnScrollListenerDelegate.onScrollStateChanged(view, scrollState);
                }
                if (scrollState != 2) {
                    if ((scrollState == 1 || scrollState == 0) && StickyListHeadersListView.this.getLastVisiblePosition() == StickyListHeadersListView.this.getCount() - 1 && StickyListHeadersListView.this.loadMoreListener != null) {
                        StickyListHeadersListView.this.loadMoreListener.OnLoadingMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (StickyListHeadersListView.this.mOnScrollListenerDelegate != null) {
                    StickyListHeadersListView.this.mOnScrollListenerDelegate.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                if (Build.VERSION.SDK_INT >= 8) {
                    StickyListHeadersListView.this.scrollChanged(firstVisibleItem);
                }
            }
        };
        super.setOnScrollListener(this.mOnScrollListener);
        super.setDivider(null);
        super.setDividerHeight(0);
        this.mViewConfig = ViewConfiguration.get(context);
        if (this.mClippingToPadding == null) {
            this.mClippingToPadding = true;
        }
        try {
            @SuppressLint("DiscouragedPrivateApi") Field selectorRectField = AbsListView.class.getDeclaredField("mSelectorRect");
            selectorRectField.setAccessible(true);
            this.mSelectorRect = (Rect) selectorRectField.get(this);
            this.mSelectorPositionField = AbsListView.class.getDeclaredField("mSelectorPosition");
            this.mSelectorPositionField.setAccessible(true);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void setLoadingMoreListener(OnLoadingMoreLinstener listener) {
        this.loadMoreListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            reset();
            scrollChanged(getFirstVisiblePosition());
        }
    }

    public void reset() {
        this.mHeader = null;
        this.mCurrentHeaderId = null;
        this.mHeaderPosition = null;
        this.mHeaderBottomPosition = -1;
    }

    @Override
    public boolean performItemClick(View view, int position, long id) {
        if (view instanceof WrapperView) {
            view = ((WrapperView) view).mItem;
        }
        return super.performItemClick(view, position, id);
    }

    @Override
    public void setDivider(Drawable divider) {
        int dividerDrawableHeight;
        this.mDivider = divider;
        if (divider != null && (dividerDrawableHeight = divider.getIntrinsicHeight()) >= 0) {
            setDividerHeight(dividerDrawableHeight);
        }
        if (this.mAdapter != null) {
            this.mAdapter.setDivider(divider);
            requestLayout();
            invalidate();
        }
    }

    @Override
    public void setDividerHeight(int height) {
        this.mDividerHeight = height;
        if (this.mAdapter != null) {
            this.mAdapter.setDividerHeight(height);
            requestLayout();
            invalidate();
        }
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        this.mOnScrollListenerDelegate = l;
    }

    public boolean getAreHeadersSticky() {
        return this.mAreHeadersSticky;
    }

    public void setAreHeadersSticky(boolean areHeadersSticky) {
        if (this.mAreHeadersSticky != areHeadersSticky) {
            this.mAreHeadersSticky = areHeadersSticky;
            requestLayout();
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (isInEditMode()) {
            super.setAdapter(adapter);
        } else if (adapter == null) {
            this.mAdapter = null;
            reset();
            super.setAdapter(null);
        } else if (!(adapter instanceof StickyListHeadersAdapter)) {
            throw new IllegalArgumentException("Adapter must implement StickyListHeadersAdapter");
        } else {
            this.mAdapter = wrapAdapter(adapter);
            reset();
            super.setAdapter(this.mAdapter);
        }
    }

    @NonNull
    private AdapterWrapper wrapAdapter(ListAdapter adapter) {
        AdapterWrapper wrapper;
        if (adapter instanceof SectionIndexer) {
            wrapper = new SectionIndexerAdapterWrapper(getContext(), (StickyListHeadersAdapter) adapter);
        } else {
            wrapper = new AdapterWrapper(getContext(), (StickyListHeadersAdapter) adapter);
        }
        wrapper.setDivider(this.mDivider);
        wrapper.setDividerHeight(this.mDividerHeight);
        wrapper.registerDataSetObserver(this.mDataSetChangedObserver);
        wrapper.setOnHeaderClickListener(this.mAdapterHeaderClickListener);
        return wrapper;
    }

    public StickyListHeadersAdapter getWrappedAdapter() {
        if (this.mAdapter == null) {
            return null;
        }
        return this.mAdapter.mDelegate;
    }

    public View getWrappedView(int position) {
        View view = getChildAt(position);
        if (view instanceof WrapperView) {
            return ((WrapperView) view).mItem;
        }
        return view;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT < 8) {
            scrollChanged(getFirstVisiblePosition());
        }
        positionSelectorRect();
        if (!this.mAreHeadersSticky || this.mHeader == null) {
            super.dispatchDraw(canvas);
            return;
        }
        if (!this.mDrawingListUnderStickyHeader) {
            this.mClippingRect.set(0, this.mHeaderBottomPosition, getWidth(), getHeight());
            canvas.save();
            canvas.clipRect(this.mClippingRect);
        }
        super.dispatchDraw(canvas);
        if (!this.mDrawingListUnderStickyHeader) {
            canvas.restore();
        }
        drawStickyHeader(canvas);
    }

    private void positionSelectorRect() {
        int selectorPosition;
        if (!this.mSelectorRect.isEmpty() && (selectorPosition = getSelectorPosition()) >= 0) {
            int firstVisibleItem = fixedFirstVisibleItem(getFirstVisiblePosition());
            View v = getChildAt(selectorPosition - firstVisibleItem);
            if (v instanceof WrapperView wrapper) {
                this.mSelectorRect.top = wrapper.getTop() + wrapper.mItemTop;
            }
        }
    }

    private int getSelectorPosition() {
        if (this.mSelectorPositionField == null) {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i).getBottom() == this.mSelectorRect.bottom) {
                    return fixedFirstVisibleItem(getFirstVisiblePosition()) + i;
                }
            }
        } else {
            try {
                return this.mSelectorPositionField.getInt(this);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private void drawStickyHeader(@NonNull Canvas canvas) {
        int headerHeight = getHeaderHeight();
        int top = this.mHeaderBottomPosition - headerHeight;
        this.mClippingRect.left = getPaddingLeft();
        this.mClippingRect.right = getWidth() - getPaddingRight();
        this.mClippingRect.bottom = top + headerHeight;
        this.mClippingRect.top = this.mClippingToPadding ? getPaddingTop() : 0;
        canvas.save();
        canvas.clipRect(this.mClippingRect);
        canvas.translate(getPaddingLeft(), top);
        this.mHeader.draw(canvas);
        canvas.restore();
    }

    private void measureHeader() {
        int heightMeasureSpec;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(((getWidth() - getPaddingLeft()) - getPaddingRight()) - (isScrollBarOverlay() ? 0 : getVerticalScrollbarWidth()), MeasureSpec.EXACTLY);
        ViewGroup.LayoutParams params = this.mHeader.getLayoutParams();
        if (params != null && params.height > 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        this.mHeader.measure(widthMeasureSpec, heightMeasureSpec);
        this.mHeader.layout(getPaddingLeft(), 0, getWidth() - getPaddingRight(), this.mHeader.getMeasuredHeight());
    }

    private boolean isScrollBarOverlay() {
        int scrollBarStyle = getScrollBarStyle();
        return scrollBarStyle == 0 || scrollBarStyle == 33554432;
    }

    private int getHeaderHeight() {
        if (this.mHeader == null) {
            return 0;
        }
        return this.mHeader.getMeasuredHeight();
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        this.mClippingToPadding = clipToPadding;
    }

    public void scrollChanged(int reportedFirstVisibleItem) {
        Log.e("parent", "scrollChanged");
        int adapterCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        if (adapterCount != 0 && this.mAreHeadersSticky) {
            int listViewHeaderCount = getHeaderViewsCount();
            int firstVisibleItem = fixedFirstVisibleItem(reportedFirstVisibleItem) - listViewHeaderCount;
            if (firstVisibleItem < 0 || firstVisibleItem > adapterCount - 1) {
                reset();
                updateHeaderVisibilities();
                invalidate();
                return;
            }
            if (this.mHeaderPosition == null || this.mHeaderPosition != firstVisibleItem) {
                this.mHeaderPosition = firstVisibleItem;
                this.mCurrentHeaderId = this.mAdapter.getHeaderId(firstVisibleItem);
                Log.i("zhej", "scrollChanged: " + this.mHeaderPosition);
                this.mHeader = this.mAdapter.getHeaderView(this.mHeaderPosition, this.mHeader, this, true);
                measureHeader();
            }
            int childCount = getChildCount();
            if (childCount != 0) {
                View viewToWatch = null;
                int watchingChildDistance = Integer.MAX_VALUE;
                boolean viewToWatchIsFooter = false;
                for (int i = 0; i < childCount; i++) {
                    View child = super.getChildAt(i);
                    boolean childIsFooter = this.mFooterViews != null && this.mFooterViews.contains(child);
                    int childDistance = child.getTop() - (this.mClippingToPadding ? getPaddingTop() : 0);
                    if (childDistance >= 0 && (viewToWatch == null || ((!viewToWatchIsFooter && !((WrapperView) viewToWatch).hasHeader()) || ((childIsFooter || ((WrapperView) child).hasHeader()) && childDistance < watchingChildDistance)))) {
                        viewToWatch = child;
                        viewToWatchIsFooter = childIsFooter;
                        watchingChildDistance = childDistance;
                    }
                }
                int headerHeight = getHeaderHeight();
                if (viewToWatch == null || !(viewToWatchIsFooter || ((WrapperView) viewToWatch).hasHeader())) {
                    this.mHeaderBottomPosition = (this.mClippingToPadding ? getPaddingTop() : 0) + headerHeight;
                } else if (firstVisibleItem == listViewHeaderCount && super.getChildAt(0).getTop() > 0 && !this.mClippingToPadding) {
                    this.mHeaderBottomPosition = 0;
                } else {
                    int paddingTop = this.mClippingToPadding ? getPaddingTop() : 0;
                    this.mHeaderBottomPosition = Math.min(viewToWatch.getTop(), headerHeight + paddingTop);
                    this.mHeaderBottomPosition = this.mHeaderBottomPosition < paddingTop ? headerHeight + paddingTop : this.mHeaderBottomPosition;
                }
            }
            updateHeaderVisibilities();
            invalidate();
        }
    }

    @Override
    public void addFooterView(View v) {
        super.addFooterView(v);
        if (this.mFooterViews == null) {
            this.mFooterViews = new ArrayList<>();
        }
        this.mFooterViews.add(v);
    }

    @Override
    public boolean removeFooterView(View v) {
        if (super.removeFooterView(v)) {
            this.mFooterViews.remove(v);
            return true;
        }
        return false;
    }

    private void updateHeaderVisibilities() {
        int top = this.mClippingToPadding ? getPaddingTop() : 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = super.getChildAt(i);
            if (child instanceof WrapperView wrapperViewChild) {
                if (wrapperViewChild.hasHeader()) {
                    View childHeader = wrapperViewChild.mHeader;
                    if (wrapperViewChild.getTop() < top) {
                        childHeader.setVisibility(View.INVISIBLE);
                    } else {
                        childHeader.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private int fixedFirstVisibleItem(int firstVisibleItem) {
        if (Build.VERSION.SDK_INT >= 11) {
            return firstVisibleItem;
        }
        int i = 0;
        while (true) {
            if (i >= getChildCount()) {
                break;
            } else if (getChildAt(i).getBottom() < 0) {
                i++;
            } else {
                firstVisibleItem += i;
                break;
            }
        }
        if (!this.mClippingToPadding && getPaddingTop() > 0 && super.getChildAt(0).getTop() > 0 && firstVisibleItem > 0) {
            firstVisibleItem--;
        }
        return firstVisibleItem;
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.mOnHeaderClickListener = onHeaderClickListener;
    }

    public boolean isDrawingListUnderStickyHeader() {
        return this.mDrawingListUnderStickyHeader;
    }

    public void setDrawingListUnderStickyHeader(boolean drawingListUnderStickyHeader) {
        this.mDrawingListUnderStickyHeader = drawingListUnderStickyHeader;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        int action = ev.getAction();
        if (action == 0 && ev.getY() <= this.mHeaderBottomPosition) {
            this.mHeaderDownY = ev.getY();
            this.mHeaderBeingPressed = true;
            this.mHeader.setPressed(true);
            this.mHeader.invalidate();
            invalidate(0, 0, getWidth(), this.mHeaderBottomPosition);
            return true;
        }
        if (this.mHeaderBeingPressed) {
            if (Math.abs(ev.getY() - this.mHeaderDownY) < this.mViewConfig.getScaledTouchSlop()) {
                if (action == 1 || action == 3) {
                    this.mHeaderDownY = -1.0f;
                    this.mHeaderBeingPressed = false;
                    this.mHeader.setPressed(false);
                    this.mHeader.invalidate();
                    invalidate(0, 0, getWidth(), this.mHeaderBottomPosition);
                    if (this.mOnHeaderClickListener != null) {
                        this.mOnHeaderClickListener.onHeaderClick(this, this.mHeader, this.mHeaderPosition, this.mCurrentHeaderId, true);
                        return true;
                    }
                    return true;
                }
                return true;
            }
            this.mHeaderDownY = -1.0f;
            this.mHeaderBeingPressed = false;
            this.mHeader.setPressed(false);
            this.mHeader.invalidate();
            invalidate(0, 0, getWidth(), this.mHeaderBottomPosition);
        }
        return super.onTouchEvent(ev);
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(StickyListHeadersListView stickyListHeadersListView, View view, int i, long j, boolean z);
    }

    public interface OnLoadingMoreLinstener {
        void OnLoadingMore();
    }
}

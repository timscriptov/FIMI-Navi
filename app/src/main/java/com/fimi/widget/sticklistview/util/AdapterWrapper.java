package com.fimi.widget.sticklistview.util;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class AdapterWrapper extends BaseAdapter implements StickyListHeadersAdapter {
    final StickyListHeadersAdapter mDelegate;
    private final Context mContext;
    private final List<View> mHeaderCache = new LinkedList();
    private Drawable mDivider;
    private int mDividerHeight;
    private OnHeaderClickListener mOnHeaderClickListener;
    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onInvalidated() {
            AdapterWrapper.this.mHeaderCache.clear();
            AdapterWrapper.super.notifyDataSetInvalidated();
        }

        @Override
        public void onChanged() {
            AdapterWrapper.super.notifyDataSetChanged();
        }
    };

    public AdapterWrapper(Context context, @NonNull StickyListHeadersAdapter delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
        delegate.registerDataSetObserver(this.mDataSetObserver);
    }

    public void setDivider(Drawable divider) {
        this.mDivider = divider;
    }

    public void setDividerHeight(int dividerHeight) {
        this.mDividerHeight = dividerHeight;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return this.mDelegate.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return this.mDelegate.isEnabled(position);
    }

    @Override
    public int getCount() {
        return this.mDelegate.getCount();
    }

    @Override
    public Object getItem(int position) {
        return this.mDelegate.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return this.mDelegate.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return this.mDelegate.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return this.mDelegate.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return this.mDelegate.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return this.mDelegate.isEmpty();
    }

    private void recycleHeaderIfExists(@NonNull WrapperView wv) {
        View header = wv.mHeader;
        if (header != null) {
            this.mHeaderCache.add(header);
        }
    }

    @NonNull
    private View configureHeader(WrapperView wv, final int position) {
        View header = this.mDelegate.getHeaderView(position, wv.mHeader == null ? popHeader() : wv.mHeader, wv, false);
        if (header == null) {
            throw new NullPointerException("Header view must not be null.");
        }
        header.setClickable(true);
        header.setOnClickListener(v -> {
            if (AdapterWrapper.this.mOnHeaderClickListener != null) {
                long headerId = AdapterWrapper.this.mDelegate.getHeaderId(position);
                AdapterWrapper.this.mOnHeaderClickListener.onHeaderClick(v, position, headerId);
            }
        });
        return header;
    }

    @Nullable
    private View popHeader() {
        if (this.mHeaderCache.size() > 0) {
            return this.mHeaderCache.remove(0);
        }
        return null;
    }

    private boolean previousPositionHasSameHeader(int position) {
        return position != 0 && this.mDelegate.getHeaderId(position) == this.mDelegate.getHeaderId(position + (-1));
    }

    @Override
    public WrapperView getView(int position, View convertView, ViewGroup parent) {
        WrapperView wv = convertView == null ? new WrapperView(this.mContext) : (WrapperView) convertView;
        View item = this.mDelegate.getView(position, wv.mItem, wv);
        View header = null;
        if (previousPositionHasSameHeader(position)) {
            recycleHeaderIfExists(wv);
        } else {
            header = configureHeader(wv, position);
        }
        if ((item instanceof Checkable) && !(wv instanceof CheckableWrapperView)) {
            wv = new CheckableWrapperView(this.mContext);
        } else if (!(item instanceof Checkable) && (wv instanceof CheckableWrapperView)) {
            wv = new WrapperView(this.mContext);
        }
        wv.update(item, header, this.mDivider, this.mDividerHeight);
        return wv;
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.mOnHeaderClickListener = onHeaderClickListener;
    }

    public boolean equals(Object o) {
        return this.mDelegate.equals(o);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return ((BaseAdapter) this.mDelegate).getDropDownView(position, convertView, parent);
    }

    public int hashCode() {
        return this.mDelegate.hashCode();
    }

    @Override
    public void notifyDataSetChanged() {
        ((BaseAdapter) this.mDelegate).notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        ((BaseAdapter) this.mDelegate).notifyDataSetInvalidated();
    }

    @NonNull
    public String toString() {
        return this.mDelegate.toString();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent, boolean isScroll) {
        return this.mDelegate.getHeaderView(position, convertView, parent, isScroll);
    }

    @Override
    public long getHeaderId(int position) {
        return this.mDelegate.getHeaderId(position);
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(View view, int i, long j);
    }
}

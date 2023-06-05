package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.fimi.android.app.R;

import java.util.ArrayList;
import java.util.List;

public class LabelsView extends ViewGroup implements View.OnClickListener {
    private static final String KEY_BG_RES_ID_STATE = "key_bg_res_id_state";
    private static final String KEY_COMPULSORY_LABELS_STATE = "key_select_compulsory_state";
    private static final String KEY_LABELS_STATE = "key_labels_state";
    private static final String KEY_LINE_MARGIN_STATE = "key_line_margin_state";
    private static final String KEY_MAX_SELECT_STATE = "key_max_select_state";
    private static final String KEY_PADDING_STATE = "key_padding_state";
    private static final String KEY_SELECT_LABELS_STATE = "key_select_labels_state";
    private static final String KEY_SELECT_TYPE_STATE = "key_select_type_state";
    private static final String KEY_SUPER_STATE = "key_super_state";
    private static final String KEY_TEXT_COLOR_STATE = "key_text_color_state";
    private static final String KEY_TEXT_SIZE_STATE = "key_text_size_state";
    private static final String KEY_WORD_MARGIN_STATE = "key_word_margin_state";
    private static final int KEY_DATA = 0x7f100010;
    private static final int KEY_POSITION = 0x7f100011;
    private final ArrayList<Integer> mCompulsorys;
    private final Context mContext;
    private final ArrayList<Object> mLabels;
    private final ArrayList<Integer> mSelectLabels;
    private Drawable mLabelBg;
    private OnLabelClickListener mLabelClickListener;
    private OnLabelSelectChangeListener mLabelSelectChangeListener;
    private int mLineMargin;
    private int mMaxSelect;
    private SelectType mSelectType;
    private ColorStateList mTextColor;
    private int mTextPaddingBottom;
    private int mTextPaddingLeft;
    private int mTextPaddingRight;
    private int mTextPaddingTop;
    private float mTextSize;
    private int mWordMargin;

    public LabelsView(Context context) {
        super(context);
        this.mLabels = new ArrayList<>();
        this.mSelectLabels = new ArrayList<>();
        this.mCompulsorys = new ArrayList<>();
        this.mContext = context;
    }

    public LabelsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLabels = new ArrayList<>();
        this.mSelectLabels = new ArrayList<>();
        this.mCompulsorys = new ArrayList<>();
        this.mContext = context;
        getAttrs(context, attrs);
    }

    public LabelsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mLabels = new ArrayList<>();
        this.mSelectLabels = new ArrayList<>();
        this.mCompulsorys = new ArrayList<>();
        this.mContext = context;
        getAttrs(context, attrs);
    }

    public static int sp2px(@NonNull Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelsView);
            int selectTypeValue = mTypedArray.getInt(R.styleable.LabelsView_selectType, SelectType.NONE.value);
            this.mSelectType = SelectType.get(selectTypeValue);
            this.mMaxSelect = mTypedArray.getInteger(R.styleable.LabelsView_maxSelect, 0);
            this.mTextColor = mTypedArray.getColorStateList(R.styleable.LabelsView_labelTextColor);
            this.mTextSize = mTypedArray.getDimension(R.styleable.LabelsView_labelTextSize, sp2px(context, 14.0f));
            this.mTextPaddingLeft = mTypedArray.getDimensionPixelOffset(R.styleable.LabelsView_labelTextPaddingLeft, 0);
            this.mTextPaddingTop = mTypedArray.getDimensionPixelOffset(R.styleable.LabelsView_labelTextPaddingTop, 0);
            this.mTextPaddingRight = mTypedArray.getDimensionPixelOffset(R.styleable.LabelsView_labelTextPaddingRight, 0);
            this.mTextPaddingBottom = mTypedArray.getDimensionPixelOffset(R.styleable.LabelsView_labelTextPaddingBottom, 0);
            this.mLineMargin = mTypedArray.getDimensionPixelOffset(R.styleable.LabelsView_lineMargin, 0);
            this.mWordMargin = mTypedArray.getDimensionPixelOffset(R.styleable.LabelsView_wordMargin, 0);
            int labelBgResId = mTypedArray.getResourceId(R.styleable.LabelsView_labelBackground, 0);
            if (labelBgResId != 0) {
                this.mLabelBg = getResources().getDrawable(labelBgResId);
            } else {
                int labelBgColor = mTypedArray.getColor(R.styleable.LabelsView_labelBackground, 0);
                this.mLabelBg = new ColorDrawable(labelBgColor);
            }
            mTypedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxWidth = (View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()) - getPaddingRight();
        int contentHeight = 0;
        int lineWidth = 0;
        int maxLineWidth = 0;
        int maxItemHeight = 0;
        boolean begin = true;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            if (!begin) {
                lineWidth += this.mWordMargin;
            } else {
                begin = false;
            }
            if (maxWidth <= view.getMeasuredWidth() + lineWidth) {
                contentHeight = contentHeight + this.mLineMargin + maxItemHeight;
                maxItemHeight = 0;
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                lineWidth = 0;
                begin = true;
            }
            maxItemHeight = Math.max(maxItemHeight, view.getMeasuredHeight());
            lineWidth += view.getMeasuredWidth();
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec, Math.max(maxLineWidth, lineWidth)), measureHeight(heightMeasureSpec, contentHeight + maxItemHeight));
    }

    private int measureWidth(int measureSpec, int contentWidth) {
        int result;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            result = specSize;
        } else {
            result = getPaddingLeft() + contentWidth + getPaddingRight();
            if (specMode == Integer.MIN_VALUE) {
                result = Math.min(result, specSize);
            }
        }
        return Math.max(result, getSuggestedMinimumWidth());
    }

    private int measureHeight(int measureSpec, int contentHeight) {
        int result;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            result = specSize;
        } else {
            result = getPaddingTop() + contentHeight + getPaddingBottom();
            if (specMode == Integer.MIN_VALUE) {
                result = Math.min(result, specSize);
            }
        }
        return Math.max(result, getSuggestedMinimumHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int x = getPaddingLeft();
        int y = getPaddingTop();
        int contentWidth = right - left;
        int maxItemHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (contentWidth < view.getMeasuredWidth() + x + getPaddingRight()) {
                x = getPaddingLeft();
                y = y + this.mLineMargin + maxItemHeight;
                maxItemHeight = 0;
            }
            view.layout(x, y, view.getMeasuredWidth() + x, view.getMeasuredHeight() + y);
            x = x + view.getMeasuredWidth() + this.mWordMargin;
            maxItemHeight = Math.max(maxItemHeight, view.getMeasuredHeight());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        if (this.mTextColor != null) {
            bundle.putParcelable(KEY_TEXT_COLOR_STATE, this.mTextColor);
        }
        bundle.putFloat(KEY_TEXT_SIZE_STATE, this.mTextSize);
        bundle.putIntArray(KEY_PADDING_STATE, new int[]{this.mTextPaddingLeft, this.mTextPaddingTop, this.mTextPaddingRight, this.mTextPaddingBottom});
        bundle.putInt(KEY_WORD_MARGIN_STATE, this.mWordMargin);
        bundle.putInt(KEY_LINE_MARGIN_STATE, this.mLineMargin);
        bundle.putInt(KEY_SELECT_TYPE_STATE, this.mSelectType.value);
        bundle.putInt(KEY_MAX_SELECT_STATE, this.mMaxSelect);
        if (!this.mSelectLabels.isEmpty()) {
            bundle.putIntegerArrayList(KEY_SELECT_LABELS_STATE, this.mSelectLabels);
        }
        if (!this.mCompulsorys.isEmpty()) {
            bundle.putIntegerArrayList(KEY_COMPULSORY_LABELS_STATE, this.mCompulsorys);
        }
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle bundle) {
            super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER_STATE));
            ColorStateList color = bundle.getParcelable(KEY_TEXT_COLOR_STATE);
            if (color != null) {
                setLabelTextColor(color);
            }
            setLabelTextSize(bundle.getFloat(KEY_TEXT_SIZE_STATE, this.mTextSize));
            int[] padding = bundle.getIntArray(KEY_PADDING_STATE);
            if (padding != null && padding.length == 4) {
                setLabelTextPadding(padding[0], padding[1], padding[2], padding[3]);
            }
            setWordMargin(bundle.getInt(KEY_WORD_MARGIN_STATE, this.mWordMargin));
            setLineMargin(bundle.getInt(KEY_LINE_MARGIN_STATE, this.mLineMargin));
            setSelectType(SelectType.get(bundle.getInt(KEY_SELECT_TYPE_STATE, this.mSelectType.value)));
            setMaxSelect(bundle.getInt(KEY_MAX_SELECT_STATE, this.mMaxSelect));
            ArrayList<Integer> compulsory = bundle.getIntegerArrayList(KEY_COMPULSORY_LABELS_STATE);
            if (compulsory != null && !compulsory.isEmpty()) {
                setCompulsorys(compulsory);
            }
            ArrayList<Integer> selectLabel = bundle.getIntegerArrayList(KEY_SELECT_LABELS_STATE);
            if (selectLabel != null && !selectLabel.isEmpty()) {
                int size = selectLabel.size();
                int[] positions = new int[size];
                for (int i = 0; i < size; i++) {
                    positions[i] = selectLabel.get(i);
                }
                setSelects(positions);
                return;
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public <T> void setLabels(List<T> labels, LabelTextProvider<T> provider) {
        innerClearAllSelect();
        removeAllViews();
        this.mLabels.clear();
        if (labels != null) {
            this.mLabels.addAll(labels);
            int size = labels.size();
            for (int i = 0; i < size; i++) {
                addLabel(labels.get(i), i, provider);
            }
            ensureLabelClickable();
        }
        if (this.mSelectType == SelectType.SINGLE_IRREVOCABLY) {
            setSelects(0);
        }
    }

    public <T> List<T> getLabels() {
        return (List<T>) this.mLabels;
    }

    public void setLabels(List<String> labels) {
        setLabels(labels, (label, position, data) -> data.trim());
    }

    private <T> void addLabel(T data, int position, @NonNull LabelTextProvider<T> provider) {
        TextView label = new TextView(this.mContext);
        label.setPadding(this.mTextPaddingLeft, this.mTextPaddingTop, this.mTextPaddingRight, this.mTextPaddingBottom);
        label.setTextSize(0, this.mTextSize);
        label.setTextColor(this.mTextColor != null ? this.mTextColor : ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK));
        label.setBackgroundDrawable(this.mLabelBg.getConstantState().newDrawable());
        label.setTag(KEY_DATA, data);
        label.setTag(KEY_POSITION, position);
        label.setOnClickListener(this);
        addView(label);
        label.setText(provider.getLabelText(label, position, data));
    }

    private void ensureLabelClickable() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TextView label = (TextView) getChildAt(i);
            label.setClickable(this.mLabelClickListener != null || this.mSelectType != SelectType.NONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView label) {
            if (this.mSelectType != SelectType.NONE) {
                if (label.isSelected()) {
                    if (this.mSelectType != SelectType.SINGLE_IRREVOCABLY && !this.mCompulsorys.contains((Integer) label.getTag(KEY_POSITION))) {
                        setLabelSelect(label, false);
                    }
                } else if (this.mSelectType == SelectType.SINGLE || this.mSelectType == SelectType.SINGLE_IRREVOCABLY) {
                    innerClearAllSelect();
                    setLabelSelect(label, true);
                } else if (this.mSelectType == SelectType.MULTI && (this.mMaxSelect <= 0 || this.mMaxSelect > this.mSelectLabels.size())) {
                    setLabelSelect(label, true);
                }
            }
            if (this.mLabelClickListener != null) {
                this.mLabelClickListener.onLabelClick(label, label.getTag(KEY_DATA), (Integer) label.getTag(KEY_POSITION));
            }
        }
    }

    private void setLabelSelect(@NonNull TextView label, boolean isSelect) {
        if (label.isSelected() != isSelect) {
            label.setSelected(isSelect);
            if (isSelect) {
                this.mSelectLabels.add((Integer) label.getTag(KEY_POSITION));
            } else {
                this.mSelectLabels.remove((Integer) label.getTag(KEY_POSITION));
            }
            if (this.mLabelSelectChangeListener != null) {
                this.mLabelSelectChangeListener.onLabelSelectChange(label, label.getTag(KEY_DATA), isSelect, (Integer) label.getTag(KEY_POSITION));
            }
        }
    }

    public void clearAllSelect() {
        if (this.mSelectType != SelectType.SINGLE_IRREVOCABLY) {
            if (this.mSelectType == SelectType.MULTI && !this.mCompulsorys.isEmpty()) {
                clearNotCompulsorySelect();
            } else {
                innerClearAllSelect();
            }
        }
    }

    private void innerClearAllSelect() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            setLabelSelect((TextView) getChildAt(i), false);
        }
        this.mSelectLabels.clear();
    }

    private void clearNotCompulsorySelect() {
        int count = getChildCount();
        List<Integer> temps = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (!this.mCompulsorys.contains(i)) {
                setLabelSelect((TextView) getChildAt(i), false);
                temps.add(i);
            }
        }
        this.mSelectLabels.removeAll(temps);
    }

    public void setSelects(List<Integer> positions) {
        if (positions != null) {
            int size = positions.size();
            int[] ps = new int[size];
            for (int i = 0; i < size; i++) {
                ps[i] = positions.get(i);
            }
            setSelects(ps);
        }
    }

    public void setSelects(int... positions) {
        if (this.mSelectType != SelectType.NONE) {
            ArrayList<TextView> selectLabels = new ArrayList<>();
            int count = getChildCount();
            int size = (this.mSelectType == SelectType.SINGLE || this.mSelectType == SelectType.SINGLE_IRREVOCABLY) ? 1 : this.mMaxSelect;
            for (int p : positions) {
                if (p < count) {
                    TextView label = (TextView) getChildAt(p);
                    if (!selectLabels.contains(label)) {
                        setLabelSelect(label, true);
                        selectLabels.add(label);
                    }
                    if (size > 0 && selectLabels.size() == size) {
                        break;
                    }
                }
            }
            for (int i = 0; i < count; i++) {
                TextView label2 = (TextView) getChildAt(i);
                if (!selectLabels.contains(label2)) {
                    setLabelSelect(label2, false);
                }
            }
        }
    }

    public List<Integer> getCompulsorys() {
        return this.mCompulsorys;
    }

    public void setCompulsorys(List<Integer> positions) {
        if (this.mSelectType == SelectType.MULTI && positions != null) {
            this.mCompulsorys.clear();
            this.mCompulsorys.addAll(positions);
            innerClearAllSelect();
            setSelects(positions);
        }
    }

    public void setCompulsorys(int... positions) {
        if (this.mSelectType == SelectType.MULTI && positions != null) {
            List<Integer> ps = new ArrayList<>(positions.length);
            for (int i : positions) {
                ps.add(i);
            }
            setCompulsorys(ps);
        }
    }

    public void clearCompulsorys() {
        if (this.mSelectType == SelectType.MULTI && !this.mCompulsorys.isEmpty()) {
            this.mCompulsorys.clear();
            innerClearAllSelect();
        }
    }

    public List<Integer> getSelectLabels() {
        return this.mSelectLabels;
    }

    public <T> List<T> getSelectLabelDatas() {
        ArrayList<T> arrayList = new ArrayList<>();
        int size = this.mSelectLabels.size();
        for (int i = 0; i < size; i++) {
            View label = getChildAt(this.mSelectLabels.get(i));
            Object data = label.getTag(KEY_DATA);
            if (data != null) {
                arrayList.add((T) data);
            }
        }
        return arrayList;
    }

    public void setLabelBackgroundResource(int resId) {
        setLabelBackgroundDrawable(getResources().getDrawable(resId));
    }

    public void setLabelBackgroundColor(int color) {
        setLabelBackgroundDrawable(new ColorDrawable(color));
    }

    public void setLabelBackgroundDrawable(Drawable drawable) {
        this.mLabelBg = drawable;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TextView label = (TextView) getChildAt(i);
            label.setBackgroundDrawable(this.mLabelBg.getConstantState().newDrawable());
        }
    }

    public void setLabelTextPadding(int left, int top, int right, int bottom) {
        if (this.mTextPaddingLeft != left || this.mTextPaddingTop != top || this.mTextPaddingRight != right || this.mTextPaddingBottom != bottom) {
            this.mTextPaddingLeft = left;
            this.mTextPaddingTop = top;
            this.mTextPaddingRight = right;
            this.mTextPaddingBottom = bottom;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                TextView label = (TextView) getChildAt(i);
                label.setPadding(left, top, right, bottom);
            }
        }
    }

    public int getTextPaddingLeft() {
        return this.mTextPaddingLeft;
    }

    public int getTextPaddingTop() {
        return this.mTextPaddingTop;
    }

    public int getTextPaddingRight() {
        return this.mTextPaddingRight;
    }

    public int getTextPaddingBottom() {
        return this.mTextPaddingBottom;
    }

    public float getLabelTextSize() {
        return this.mTextSize;
    }

    public void setLabelTextSize(float size) {
        if (this.mTextSize != size) {
            this.mTextSize = size;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                TextView label = (TextView) getChildAt(i);
                label.setTextSize(0, size);
            }
        }
    }

    public ColorStateList getLabelTextColor() {
        return this.mTextColor;
    }

    public void setLabelTextColor(int color) {
        setLabelTextColor(ColorStateList.valueOf(color));
    }

    public void setLabelTextColor(ColorStateList color) {
        this.mTextColor = color;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TextView label = (TextView) getChildAt(i);
            label.setTextColor(this.mTextColor != null ? this.mTextColor : ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK));
        }
    }

    public int getLineMargin() {
        return this.mLineMargin;
    }

    public void setLineMargin(int margin) {
        if (this.mLineMargin != margin) {
            this.mLineMargin = margin;
            requestLayout();
        }
    }

    public int getWordMargin() {
        return this.mWordMargin;
    }

    public void setWordMargin(int margin) {
        if (this.mWordMargin != margin) {
            this.mWordMargin = margin;
            requestLayout();
        }
    }

    public SelectType getSelectType() {
        return this.mSelectType;
    }

    public void setSelectType(SelectType selectType) {
        if (this.mSelectType != selectType) {
            this.mSelectType = selectType;
            innerClearAllSelect();
            if (this.mSelectType == SelectType.SINGLE_IRREVOCABLY) {
                setSelects(0);
            }
            if (this.mSelectType != SelectType.MULTI) {
                this.mCompulsorys.clear();
            }
            ensureLabelClickable();
        }
    }

    public int getMaxSelect() {
        return this.mMaxSelect;
    }

    public void setMaxSelect(int maxSelect) {
        if (this.mMaxSelect != maxSelect) {
            this.mMaxSelect = maxSelect;
            if (this.mSelectType == SelectType.MULTI) {
                innerClearAllSelect();
            }
        }
    }

    public void setOnLabelClickListener(OnLabelClickListener l) {
        this.mLabelClickListener = l;
        ensureLabelClickable();
    }

    public void setOnLabelSelectChangeListener(OnLabelSelectChangeListener l) {
        this.mLabelSelectChangeListener = l;
    }

    public enum SelectType {
        NONE(1),
        SINGLE(2),
        SINGLE_IRREVOCABLY(3),
        MULTI(4);

        final int value;

        SelectType(int value) {
            this.value = value;
        }

        static SelectType get(int value) {
            switch (value) {
                case 1:
                    return NONE;
                case 2:
                    return SINGLE;
                case 3:
                    return SINGLE_IRREVOCABLY;
                case 4:
                    return MULTI;
                default:
                    return NONE;
            }
        }
    }

    public interface LabelTextProvider<T> {
        CharSequence getLabelText(TextView textView, int i, T t);
    }

    public interface OnLabelClickListener {
        void onLabelClick(TextView textView, Object obj, int i);
    }

    public interface OnLabelSelectChangeListener {
        void onLabelSelectChange(TextView textView, Object obj, boolean z, int i);
    }
}

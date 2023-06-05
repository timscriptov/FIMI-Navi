package com.fimi.libperson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.fimi.android.app.R;
import com.fimi.widget.sticklistview.SortModel;
import com.fimi.widget.sticklistview.util.StickyListHeadersAdapter;

import org.slf4j.Marker;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CountryLetterSortAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer, Filterable {
    private static final String TAG = CountryLetterSortAdapter.class.getSimpleName();
    private Context mContext;
    private List<SortModel> mList;
    private List<SortModel> mListAll = new ArrayList();
    private OnShowLetterChangedListener mOnShowLetterChangedListener;

    public CountryLetterSortAdapter(Context mContext, List<SortModel> list) {
        this.mList = null;
        this.mContext = mContext;
        this.mList = list;
        this.mListAll.clear();
        this.mListAll.addAll(list);
    }

    public void updateListView(List<SortModel> list) {
        this.mList = list;
        this.mListAll = list;
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(this.mContext).inflate(R.layout.country_select_item_country_letter_sort, viewGroup, false);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (position < this.mList.size()) {
            viewHolder.tvTitle.setText(this.mList.get(position).getName().substring(0, this.mList.get(position).getName().lastIndexOf(Marker.ANY_MARKER)));
        }
        return view;
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: com.fimi.libperson.adapter.CountryLetterSortAdapter.1
            @Override // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                Filter.FilterResults filterResults = new Filter.FilterResults();
                ArrayList arrayList = new ArrayList();
                String search = charSequence.toString().toLowerCase();
                int n = CountryLetterSortAdapter.this.mListAll.size();
                for (int i = 0; i < n; i++) {
                    String name = ((SortModel) CountryLetterSortAdapter.this.mListAll.get(i)).getPinyin();
                    if (name.toLowerCase().startsWith(search)) {
                        arrayList.add(CountryLetterSortAdapter.this.mListAll.get(i));
                    }
                }
                filterResults.count = arrayList.size();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override // android.widget.Filter
            protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                CountryLetterSortAdapter.this.mList.clear();
                CountryLetterSortAdapter.this.mList.addAll((List) filterResults.values);
                CountryLetterSortAdapter.this.notifyDataSetChanged();
            }
        };
    }

    @Override // com.fimi.widget.sticklistview.util.StickyListHeadersAdapter
    @RequiresApi(api = 23)
    public View getHeaderView(int position, View view, ViewGroup viewGroup, boolean isScroll) {
        HeaderViewHolder headerViewHolder;
        if (view == null) {
            headerViewHolder = new HeaderViewHolder();
            view = LayoutInflater.from(this.mContext).inflate(R.layout.country_select_item_sticky_header, viewGroup, false);
            headerViewHolder.tvLetter = (TextView) view.findViewById(R.id.sticky_header_letter_tv);
            view.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) view.getTag();
        }
        headerViewHolder.tvLetter.setBackgroundColor(this.mContext.getColor(R.color.country_select_bg));
        if (isScroll) {
            this.mOnShowLetterChangedListener.onShowLetterChanged(this.mList.get(position).getSortLetter());
        }
        if (!this.mList.get(position).getSortLetter().equals("#")) {
            headerViewHolder.tvLetter.setText(this.mList.get(position).getSortLetter());
        }
        return view;
    }

    @Override // com.fimi.widget.sticklistview.util.StickyListHeadersAdapter
    public long getHeaderId(int position) {
        return this.mList.get(position).getSortLetter().subSequence(0, 1).charAt(0);
    }

    @Override // android.widget.SectionIndexer
    public int getSectionForPosition(int position) {
        return this.mList.get(position).getSortLetter().charAt(0);
    }

    @Override // android.widget.SectionIndexer
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = this.mList.get(i).getSortLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private String getAlpha(String letter) {
        String sortStr = letter.trim().substring(0, 1).toUpperCase();
        return sortStr.matches("[A-Z]") ? sortStr : "#";
    }

    @Override // android.widget.SectionIndexer
    public Object[] getSections() {
        return null;
    }

    public void setOnShowLetterChangedListener(OnShowLetterChangedListener onShowLetterChangedListener) {
        this.mOnShowLetterChangedListener = onShowLetterChangedListener;
    }

    /* loaded from: classes.dex */
    public interface OnShowLetterChangedListener {
        void onShowLetterChanged(String str);
    }

    /* loaded from: classes.dex */
    static final class HeaderViewHolder {
        TextView tvLetter;

        HeaderViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    static final class ViewHolder {
        TextView tvTitle;

        ViewHolder() {
        }
    }
}

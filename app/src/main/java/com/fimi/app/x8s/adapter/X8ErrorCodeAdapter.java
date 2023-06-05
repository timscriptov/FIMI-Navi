package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.entity.X8ErrorCode;

import java.util.List;


public class X8ErrorCodeAdapter extends BaseAdapter {
    private final Context context;
    private final List<X8ErrorCode> mList;

    public X8ErrorCodeAdapter(Context context, List<X8ErrorCode> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = holder.initView(parent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        X8ErrorCode itemBean = this.mList.get(position);
        holder.mTvItemTitle.setText(itemBean.getTitle());
        switch (itemBean.getLevel()) {
            case serious:
                holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type1));
                holder.mIvArrow.setBackgroundResource(R.drawable.x8_error_code_type1_icon);
                holder.mImgBg.setBackgroundResource(R.drawable.x8_error_code_type1);
                break;
            case medium:
                holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type2));
                holder.mIvArrow.setBackgroundResource(R.drawable.x8_error_code_type2_icon);
                holder.mImgBg.setBackgroundResource(R.drawable.x8_error_code_type2);
                break;
            case slight:
                holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type3));
                holder.mIvArrow.setBackgroundResource(R.drawable.x8_error_code_type3_icon);
                holder.mImgBg.setBackgroundResource(R.drawable.x8_error_code_type3);
                break;
        }
        return convertView;
    }


    public class ViewHolder {
        public ImageView mImgBg;
        public ImageView mIvArrow;
        public TextView mTvItemTitle;

        public ViewHolder() {
        }

        public View initView(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(X8ErrorCodeAdapter.this.context);
            View view = inflater.inflate(R.layout.x8_adapt_error_code, parent, false);
            this.mTvItemTitle = view.findViewById(R.id.tv_item_title);
            this.mIvArrow = view.findViewById(R.id.iv_arrow);
            this.mImgBg = view.findViewById(R.id.iv_bg);
            return view;
        }
    }
}

package com.fimi.libperson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.libperson.entity.PersonSetting;

import java.util.List;


public class PersettingThirdAdapt extends BaseAdapter {
    private final Context mContext;
    private List<PersonSetting> mList;

    public PersettingThirdAdapt(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (this.mList == null) {
            return 0;
        }
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<PersonSetting> list) {
        this.mList = list;
        notifyDataSetChanged();
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
        if (this.mList != null) {
            resetDefaultView(holder, convertView.getLayoutParams());
            State positionIndex = this.mList.get(position).getThirdAdapt();
            if (positionIndex == State.USER_AGREEMENT) {
                holder.mTvItemTitle.setText(R.string.person_setting_user_agreement);
            } else if (positionIndex == State.USER_PRIVACY) {
                holder.mTvItemTitle.setText(R.string.person_setting_user_privacy);
            } else if (positionIndex == State.USER_RIGHT) {
                holder.mTvItemTitle.setText(R.string.libperson_user_right);
            }
        }
        return convertView;
    }

    private void resetDefaultView(ViewHolder holder, ViewGroup.LayoutParams params) {
        holder.mIvArrow.setVisibility(View.VISIBLE);
        holder.mTvContent.setText("");
        params.height = (int) this.mContext.getResources().getDimension(R.dimen.person_setting_height);
        holder.mRlBg.setLayoutParams(params);
        holder.mRlBg.setBackgroundResource(R.drawable.person_listview_item_shape_enable);
    }


    public enum State {
        USER_AGREEMENT,
        USER_PRIVACY,
        USER_RIGHT
    }


    public class ViewHolder {
        ImageView mIvArrow;
        RelativeLayout mRlBg;
        TextView mTvContent;
        TextView mTvItemTitle;

        private ViewHolder() {
        }

        public View initView(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(PersettingThirdAdapt.this.mContext);
            View view = inflater.inflate(R.layout.libperson_adapt_person_new_setting, parent, false);
            this.mRlBg = view.findViewById(R.id.rl_bg);
            this.mTvItemTitle = view.findViewById(R.id.tv_item_title);
            this.mIvArrow = view.findViewById(R.id.iv_arrow);
            this.mTvContent = view.findViewById(R.id.tv_content);
            FontUtil.changeFontLanTing(PersettingThirdAdapt.this.mContext.getAssets(), this.mTvContent, this.mTvItemTitle);
            return view;
        }
    }
}

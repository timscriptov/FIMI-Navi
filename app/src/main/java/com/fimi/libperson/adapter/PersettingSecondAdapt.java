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
import com.fimi.kernel.Constants;
import com.fimi.kernel.language.LanguageModel;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LanguageUtil;
import com.fimi.libperson.entity.PersonSetting;

import java.util.List;


public class PersettingSecondAdapt extends BaseAdapter {
    private final Context mContext;
    private List<PersonSetting> mList;

    public PersettingSecondAdapt(Context context) {
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
            State positionIndex = this.mList.get(position).getSecondAdapt();
            if (positionIndex == State.SERVICE) {
                holder.mTvItemTitle.setText(R.string.libperson_service_setting_title);
                ServiceItem serviceItem = SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class);
                boolean isInfo = false;
                int i = 0;
                while (true) {
                    if (i >= ServiceItem.getServicename().length) {
                        break;
                    } else if (ServiceItem.getServicename()[i] != serviceItem.getInfo()) {
                        i++;
                    } else {
                        isInfo = true;
                        break;
                    }
                }
                if (isInfo) {
                    holder.mTvContent.setText(this.mContext.getString(serviceItem.getInfo()));
                } else {
                    holder.mTvContent.setText(this.mContext.getString(R.string.region_Mainland_China));
                }
            } else if (positionIndex == State.LANGUAGE) {
                holder.mTvItemTitle.setText(R.string.person_setting_language);
                LanguageModel languageModel = LanguageUtil.getCurrentLanguage();
                if (languageModel.getLanguageName() != R.string.kernal_simplified_chinese && languageModel.getLanguageName() != R.string.kernal_english && languageModel.getLanguageName() != R.string.kernal_korean && languageModel.getLanguageName() != R.string.kernal_spanish) {
                    if (languageModel.getLocale().equals("zh_CN")) {
                        languageModel.setLanguageName(R.string.kernal_simplified_chinese);
                    } else if (languageModel.getLocale().equals("en_US")) {
                        languageModel.setLanguageName(R.string.kernal_english);
                    } else if (languageModel.getLocale().equals("ko-KR")) {
                        languageModel.setLanguageName(R.string.kernal_korean);
                    } else if (languageModel.getLocale().equals("es-ES")) {
                        languageModel.setLanguageName(R.string.kernal_spanish);
                    } else {
                        languageModel.setLanguageName(R.string.kernal_simplified_chinese);
                    }
                }
                holder.mTvContent.setText(this.mContext.getString(languageModel.getLanguageName()));
            }
        }
        return convertView;
    }

    private void resetDefaultView(ViewHolder holder, ViewGroup.LayoutParams params) {
        holder.mIvArrow.setVisibility(0);
        holder.mTvContent.setText("");
        params.height = (int) this.mContext.getResources().getDimension(R.dimen.person_setting_height);
        holder.mRlBg.setLayoutParams(params);
        holder.mRlBg.setBackgroundResource(R.drawable.person_listview_item_shape_enable);
    }


    public enum State {
        SERVICE,
        LANGUAGE
    }


    public class ViewHolder {
        ImageView mIvArrow;
        RelativeLayout mRlBg;
        TextView mTvContent;
        TextView mTvItemTitle;

        private ViewHolder() {
        }

        public View initView(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(PersettingSecondAdapt.this.mContext);
            View view = inflater.inflate(R.layout.libperson_adapt_person_new_setting, parent, false);
            this.mRlBg = view.findViewById(R.id.rl_bg);
            this.mTvItemTitle = view.findViewById(R.id.tv_item_title);
            this.mIvArrow = view.findViewById(R.id.iv_arrow);
            this.mTvContent = view.findViewById(R.id.tv_content);
            FontUtil.changeFontLanTing(PersettingSecondAdapt.this.mContext.getAssets(), this.mTvContent, this.mTvItemTitle);
            return view;
        }
    }
}

package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.network.entity.UpfirewareDto;

import java.util.List;


public class UpdateContentAdapter extends BaseAdapter {
    List<UpfirewareDto> list;
    Context mContext;

    public UpdateContentAdapter(List<UpfirewareDto> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHandle;
        if (view == null) {
            viewHandle = new ViewHold();
            view = LayoutInflater.from(this.mContext).inflate(R.layout.x8_item_update_detail, null);
            viewHandle.tvSysnameFlag = view.findViewById(R.id.tv_sysname_flag);
            viewHandle.tvSysname = view.findViewById(R.id.tv_sysname);
            viewHandle.tvSyscontent = view.findViewById(R.id.tv_sys_content);
            FontUtil.changeFontLanTing(this.mContext.getAssets(), viewHandle.tvSyscontent);
            view.setTag(viewHandle);
        } else {
            viewHandle = (ViewHold) view.getTag();
        }
        viewHandle.tvSysnameFlag.setText(getHanNumber(i));
        viewHandle.tvSysname.setText(this.list.get(i).getSysName());
        viewHandle.tvSyscontent.setText(this.list.get(i).getUpdateContent());
        return view;
    }

    public void updateList(List<UpfirewareDto> needUpgrade) {
        this.list = needUpgrade;
        notifyDataSetChanged();
    }

    public String getHanNumber(int position) {
        return (position + 1) + ".";
    }


    private static class ViewHold {
        TextView tvSyscontent;
        TextView tvSysname;
        TextView tvSysnameFlag;

        private ViewHold() {
        }
    }
}

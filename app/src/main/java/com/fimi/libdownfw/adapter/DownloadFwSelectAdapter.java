package com.fimi.libdownfw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.host.Entity.DownloadFwSelectInfo;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.network.entity.UpfirewareDto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class DownloadFwSelectAdapter extends BaseAdapter {
    private Context context;
    private List<DownloadFwSelectInfo> list;
    private SelectListener mSelectListener;
    private List<UpfirewareDto> mUpfirewareDtoList;

    public DownloadFwSelectAdapter(Context context, List<DownloadFwSelectInfo> list) {
        this.mUpfirewareDtoList = new ArrayList();
        this.list = list;
        this.context = context;
        if (list != null && list.size() > 0) {
            this.mUpfirewareDtoList = list.get(0).getDtoList();
        }
    }

    public void setSelectListener(SelectListener mSelectListener) {
        this.mSelectListener = mSelectListener;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mUpfirewareDtoList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mUpfirewareDtoList.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHandle viewHandle;
        if (convertView == null) {
            viewHandle = new ViewHandle();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.list_download_fm_select_info_item, (ViewGroup) null);
            viewHandle.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHandle.tvInfo = (TextView) convertView.findViewById(R.id.tv_info);
            viewHandle.cbSelect = convertView.findViewById(R.id.cb_select);
            viewHandle.vLinOne = convertView.findViewById(R.id.v_lin_one);
            convertView.setTag(viewHandle);
        } else {
            viewHandle = (ViewHandle) convertView.getTag();
        }
        viewHandle.tvTitle.setText(this.mUpfirewareDtoList.get(position).getSysName());
        viewHandle.tvInfo.setText(this.mUpfirewareDtoList.get(position).getUpdateContent());
        FontUtil.changeFontLanTing(this.context.getAssets(), viewHandle.tvTitle, viewHandle.tvInfo);
        updateSelected();
        return convertView;
    }

    public void update(int position) {
        DownloadFwSelectInfo info = this.list.get(position);
        if (info.isSelect()) {
            this.list.get(position).setSelect(false);
        } else {
            this.list.get(position).setSelect(true);
        }
        for (DownloadFwSelectInfo downloadFwSelectInfo : this.list) {
        }
        notifyDataSetChanged();
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateSelected();
    }

    private void updateSelected() {
        if (this.mSelectListener != null) {
            boolean ret = false;
            Iterator<DownloadFwSelectInfo> it = this.list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                DownloadFwSelectInfo info = it.next();
                if (info.isSelect()) {
                    ret = true;
                    break;
                }
            }
            this.mSelectListener.onSelect(ret);
        }
    }

    public boolean hasForceSign() {
        for (DownloadFwSelectInfo info : this.list) {
            if (info.isForceSign()) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: classes.dex */
    public interface SelectListener {
        void onSelect(boolean z);
    }

    /* loaded from: classes.dex */
    private class ViewHandle {
        View cbSelect;
        TextView tvInfo;
        TextView tvTitle;
        View vLinOne;

        private ViewHandle() {
        }
    }
}

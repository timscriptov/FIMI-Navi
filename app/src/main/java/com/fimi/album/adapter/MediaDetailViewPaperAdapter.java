package com.fimi.album.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.fimi.album.biz.DataManager;
import com.fimi.album.biz.X9HandleType;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.IViewpaper;

import java.util.concurrent.CopyOnWriteArrayList;


public class MediaDetailViewPaperAdapter extends PagerAdapter {
    private final IViewpaper mIViewpaper;
    private final DataManager mdataManager = DataManager.obtain();
    private CopyOnWriteArrayList<? extends MediaModel> modelList;

    public MediaDetailViewPaperAdapter(IViewpaper mIViewpaper) {
        this.mIViewpaper = mIViewpaper;
        initData();
    }

    private void initData() {
        if (X9HandleType.isCameraView()) {
            if (this.mdataManager.getX9CameraDataNoHeadList() != null) {
                this.modelList = this.mdataManager.getX9CameraDataNoHeadList();
                return;
            }
            return;
        }
        this.modelList = this.mdataManager.getLocalDataNoHeadList();
    }

    @Override
    public int getCount() {
        if (this.modelList == null) {
            return 0;
        }
        return this.modelList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return this.mIViewpaper != null ? this.mIViewpaper.instantiateItem(container, position) : super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void deleteItem(int position) {
        this.modelList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return -2;
    }
}

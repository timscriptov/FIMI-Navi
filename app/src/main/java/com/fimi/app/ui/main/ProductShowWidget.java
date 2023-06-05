package com.fimi.app.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fimi.android.app.R;
import com.fimi.app.interfaces.IProductControllers;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ProductShowWidget extends FrameLayout implements IProductControllers {
    private final Context context;
    ChangePositionListener positionListener;
    private List<FrameLayout> frameLayouts;
    private ViewPager pager;

    public ProductShowWidget(@NonNull Context context) {
        this(context, null);
    }

    public ProductShowWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductShowWidget(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initWidget();
    }

    public void setPositionListener(ChangePositionListener positionListener) {
        this.positionListener = positionListener;
    }

    private void initWidget() {
        View view = LayoutInflater.from(this.context).inflate(R.layout.layout_cardslide, this, true);
        this.pager = view.findViewById(R.id.vp);
        this.frameLayouts = new ArrayList();
        for (int i = 0; i < HostNewMainActivity.PRODUCTCLASS.length; i++) {
            if (HostNewMainActivity.PRODUCTCLASS[i] == HostX9ProductView.class) {
                this.frameLayouts.add(new HostX9ProductView(this.context, null));
            } else if (HostNewMainActivity.PRODUCTCLASS[i] == HostGh2ProductView.class) {
                this.frameLayouts.add(new HostGh2ProductView(this.context, null));
            } else if (HostNewMainActivity.PRODUCTCLASS[i] == HostX8sProductView.class) {
                this.frameLayouts.add(new HostX8sProductView(this.context, null));
            }
        }
        this.pager.setAdapter(new KannerPagerAdapter());
        this.pager.setFocusable(true);
        this.pager.setCurrentItem(0);
        this.pager.setOnPageChangeListener(new PageChangeListener());
    }

    @Override
    public void stopAnimation() {
        if (this.frameLayouts.get(0).getClass() == HostX9ProductView.class) {
            ((HostX9ProductView) this.frameLayouts.get(0)).stopnAnimation();
        }
    }

    @Override
    public void startAnimation() {
        if (this.frameLayouts.get(0).getClass() == HostX9ProductView.class) {
            ((HostX9ProductView) this.frameLayouts.get(0)).startAnimation();
        }
    }

    public interface ChangePositionListener {
        void changePosition(int i);
    }

    public class KannerPagerAdapter extends PagerAdapter {
        KannerPagerAdapter() {
        }

        @Override
        public int getCount() {
            return ProductShowWidget.this.frameLayouts.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(ProductShowWidget.this.frameLayouts.get(position));
            return ProductShowWidget.this.frameLayouts.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(ProductShowWidget.this.frameLayouts.get(position));
        }
    }

    public class PageChangeListener implements ViewPager.OnPageChangeListener {
        private PageChangeListener() {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (ProductShowWidget.this.positionListener != null) {
                ProductShowWidget.this.positionListener.changePosition(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1:
                case 2:
                default:
            }
        }
    }
}

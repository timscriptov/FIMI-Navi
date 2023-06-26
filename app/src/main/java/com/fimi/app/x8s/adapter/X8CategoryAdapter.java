package com.fimi.app.x8s.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;


public class X8CategoryAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    public X8CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    public X8CategoryAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragments = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (this.fragments == null) {
            return null;
        }
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        if (this.fragments == null) {
            return 0;
        }
        return this.fragments.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}

package com.fimi.kernel.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/* loaded from: classes.dex */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected Context mContext;

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    protected abstract void doTrans();

    public abstract int getLayoutId();

    protected abstract void initData(View view);

    protected abstract void initMVP();

    @Override // android.support.v4.app.Fragment
    public void onAttach(Context context) {
        this.mActivity = (Activity) context;
        this.mContext = context;
        super.onAttach(context);
    }

    @Override // android.support.v4.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutView() != null) {
            return getLayoutView();
        }
        View view = inflater.inflate(getLayoutId(), (ViewGroup) null);
        initData(view);
        initMVP();
        doTrans();
        return view;
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBundle(getArguments());
        super.onViewCreated(view, savedInstanceState);
    }

    public void getBundle(Bundle bundle) {
    }

    public View getLayoutView() {
        return null;
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
    }
}

package com.fimi.album.ui.albumfragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.album.adapter.PanelRecycleAdapter;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.broadcast.DeleteItemReceiver;
import com.fimi.album.iview.INodataTip;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.presenter.BaseFragmentPresenter;
import com.fimi.album.presenter.LocalFragmentPresenter;
import com.fimi.album.ui.MediaActivity;
import com.fimi.album.widget.RBaseItemDecoration;
import com.fimi.android.app.R;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.SizeTool;

import java.lang.ref.WeakReference;


public abstract class BaseFragment extends Fragment implements INodataTip {
    protected WeakReference<Context> contextWeakReference;
    protected BaseFragmentPresenter mBaseFragmentPresenter;
    protected ISelectData mISelectData;
    protected RecyclerView mRecyclerView;
    protected PanelRecycleAdapter panelRecycleAdapter;
    private DeleteItemReceiver deleteItemReceiver;
    private ImageButton lbBottomDelect;
    private RelativeLayout rlMediaNoDataTip;
    private RelativeLayout rlMediaSelectBottom;

    abstract int getContentID();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deleteItemReceiver = new DeleteItemReceiver();
        IntentFilter intentFilter = new IntentFilter(AlbumConstant.DELETEITEMACTION);
        LocalBroadcastManager.getInstance(this.contextWeakReference.get().getApplicationContext()).registerReceiver(this.deleteItemReceiver, intentFilter);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutID = getContentID();
        View view = inflater.inflate(layoutID, null);
        initView(view);
        initData();
        initTrans();
        return view;
    }

    private void initTrans() {
        this.lbBottomDelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseFragment.this.mBaseFragmentPresenter != null) {
                    if (BaseFragment.this.mBaseFragmentPresenter.querySelectSize() > 0) {
                        BaseFragment.this.mBaseFragmentPresenter.deleteSelectFile();
                    } else {
                        Toast.makeText(BaseFragment.this.contextWeakReference.get(), R.string.no_select_file, 0).show();
                    }
                }
            }
        });
    }

    void initView(View view) {
        this.rlMediaNoDataTip = view.findViewById(R.id.media_no_data_tip);
        this.lbBottomDelect = view.findViewById(R.id.bottom_delete_ibtn);
        this.rlMediaSelectBottom = view.findViewById(R.id.media_select_bottom_rl);
        this.mRecyclerView = view.findViewById(R.id.recycleview);
        FontUtil.changeFontLanTing(this.contextWeakReference.get().getAssets(), this.lbBottomDelect);
    }

    protected void initData() {
        this.panelRecycleAdapter = new PanelRecycleAdapter(this.contextWeakReference.get(), this);
        if (this.contextWeakReference.get() != null) {
            this.mRecyclerView.setLayoutManager(new GridLayoutManager(this.contextWeakReference.get(), 4));
        }
        this.mRecyclerView.setAdapter(this.panelRecycleAdapter);
        this.mRecyclerView.setOverScrollMode(2);
        RBaseItemDecoration mRBaseItemDecoration = new RBaseItemDecoration(this.contextWeakReference.get(), SizeTool.pixToDp(2.5f, this.contextWeakReference.get()), 17170445);
        this.mRecyclerView.addItemDecoration(mRBaseItemDecoration);
        this.mRecyclerView.getItemAnimator().setChangeDuration(0L);
        initPresenter();
    }

    private void initPresenter() {
        this.mBaseFragmentPresenter = new LocalFragmentPresenter(this.mRecyclerView, this.panelRecycleAdapter, this.mISelectData, this.contextWeakReference.get());
        this.panelRecycleAdapter.setmIRecycleAdapter(this.mBaseFragmentPresenter);
        if (this.deleteItemReceiver != null) {
            this.deleteItemReceiver.setIReceiver(this.mBaseFragmentPresenter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.contextWeakReference = new WeakReference<>(context);
        if (context instanceof MediaActivity) {
            this.mISelectData = (ISelectData) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mISelectData = null;
        if (this.mBaseFragmentPresenter != null) {
            this.mBaseFragmentPresenter.canCalAsyncTask();
        }
        if (this.contextWeakReference.get() != null && this.deleteItemReceiver != null) {
            LocalBroadcastManager.getInstance(this.contextWeakReference.get().getApplicationContext()).unregisterReceiver(this.deleteItemReceiver);
        }
        if (this.panelRecycleAdapter != null) {
            this.panelRecycleAdapter.removeCallBack();
        }
    }

    public void reshAdapter() {
        if (this.panelRecycleAdapter != null) {
            this.panelRecycleAdapter.refresh();
            if (this.mBaseFragmentPresenter != null) {
                this.mBaseFragmentPresenter.refreshData();
            }
        }
    }

    public void enterSelectAllMode() {
        this.mBaseFragmentPresenter.enterSelectAllMode();
    }

    public void cancalSelectAllMode() {
        this.mBaseFragmentPresenter.cancalSelectAllMode();
    }

    public void enterSelectMode(boolean state, boolean isNeedPreform) {
        showBottomDeleteView(state);
        if (isNeedPreform) {
            this.mBaseFragmentPresenter.enterSelectMode(state);
        }
    }

    public void showBottomDeleteView(boolean state) {
        if (state) {
            this.rlMediaSelectBottom.setVisibility(View.VISIBLE);
        } else {
            this.rlMediaSelectBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void noDataTipCallback(boolean isNodata) {
        if (isNodata) {
            this.rlMediaNoDataTip.setVisibility(View.VISIBLE);
        } else {
            this.rlMediaNoDataTip.setVisibility(View.GONE);
        }
    }
}

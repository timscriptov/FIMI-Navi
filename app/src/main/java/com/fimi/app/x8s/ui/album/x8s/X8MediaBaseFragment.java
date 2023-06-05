package com.fimi.app.x8s.ui.album.x8s;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.broadcast.DeleteItemReceiver;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.INodataTip;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.widget.RBaseItemDecoration;
import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.X8sPanelRecycleAdapter;
import com.fimi.app.x8s.ui.presenter.X8CameraFragmentPrensenter;
import com.fimi.app.x8s.ui.presenter.X8LocalFragmentPresenter;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.base.BaseFragment;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.SizeTool;

import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public abstract class X8MediaBaseFragment extends BaseFragment implements INodataTip {
    protected WeakReference<Context> contextWeakReference;
    protected ImageButton lbBottomDelect;
    protected ImageButton lbBottomDownload;
    protected X8BaseMediaFragmentPresenter mBaseFragmentPresenter;
    protected ISelectData mISelectData;
    protected RecyclerView mRecyclerView;
    protected TextView mTvBottomDelete;
    protected TextView mTvBottomDownload;
    protected X8sPanelRecycleAdapter<MediaModel> panelRecycleAdapter;
    protected RelativeLayout rlMediaSelectBottom;
    private DeleteItemReceiver deleteItemReceiver;
    private boolean isCamera;
    private ProgressBar mProgressBar;
    private RelativeLayout mRlBottomBar;
    private RelativeLayout mRlDownload;
    private RelativeLayout rlMediaNoDataTip;

    abstract int getContentID();

    abstract boolean judgeTypeCurrentFragment();

    @Override // android.support.v4.app.Fragment
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deleteItemReceiver = new DeleteItemReceiver();
        IntentFilter intentFilter = new IntentFilter(AlbumConstant.DELETEITEMACTION);
        LocalBroadcastManager.getInstance(this.contextWeakReference.get().getApplicationContext()).registerReceiver(this.deleteItemReceiver, intentFilter);
        this.isCamera = judgeTypeCurrentFragment();
    }

    @Override // com.fimi.kernel.base.BaseFragment, android.support.v4.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutID = getContentID();
        View view = inflater.inflate(layoutID, (ViewGroup) null);
        initView(view);
        initData();
        initTrans();
        this.mISelectData.initComplete(this.isCamera);
        return view;
    }

    @Override // com.fimi.kernel.base.BaseFragment
    protected void doTrans() {
    }

    @Override // com.fimi.kernel.base.BaseFragment
    protected void initMVP() {
    }

    @Override // com.fimi.kernel.base.BaseFragment
    protected void initData(View view) {
    }

    @Override // com.fimi.kernel.base.BaseFragment
    public int getLayoutId() {
        return 0;
    }

    private void initTrans() {
        this.lbBottomDelect.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaBaseFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                X8DoubleCustomDialog doubleCustomDialog = new X8DoubleCustomDialog(X8MediaBaseFragment.this.mContext, X8MediaBaseFragment.this.getString(R.string.x8_album_warn_tip), X8MediaBaseFragment.this.getString(R.string.album_dialog_delete_title), X8MediaBaseFragment.this.getString(R.string.media_delete), new X8DoubleCustomDialog.onDialogButtonClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaBaseFragment.1.1
                    @Override
                    // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                    public void onLeft() {
                    }

                    @Override
                    // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                    public void onRight() {
                        if (X8MediaBaseFragment.this.isCamera) {
                            X8MediaBaseFragment.this.mBaseFragmentPresenter.deleteCameraSelectFile();
                        } else {
                            X8MediaBaseFragment.this.mBaseFragmentPresenter.deleteSelectFile();
                        }
                    }
                });
                doubleCustomDialog.show();
            }
        });
        this.lbBottomDownload.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaBaseFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (X8MediaBaseFragment.this.mBaseFragmentPresenter != null) {
                    if (X8MediaBaseFragment.this.mBaseFragmentPresenter.querySelectSize() > 0) {
                        X8MediaBaseFragment.this.mBaseFragmentPresenter.downLoadFile();
                    } else {
                        Toast.makeText(X8MediaBaseFragment.this.contextWeakReference.get(), R.string.no_select_file, 0).show();
                    }
                }
            }
        });
    }

    void initView(View view) {
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.loading);
        this.rlMediaNoDataTip = (RelativeLayout) view.findViewById(R.id.x9_media_no_data_tip);
        this.lbBottomDelect = (ImageButton) view.findViewById(R.id.ibtn_delete);
        this.lbBottomDownload = (ImageButton) view.findViewById(R.id.ibtn_download);
        this.mTvBottomDelete = (TextView) view.findViewById(R.id.tv_bottom_delete);
        this.mTvBottomDownload = (TextView) view.findViewById(R.id.tv_bottom_download);
        this.rlMediaSelectBottom = (RelativeLayout) view.findViewById(R.id.rl_bottom_bar);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        this.mRlDownload = (RelativeLayout) view.findViewById(R.id.rl_download);
        this.mRlBottomBar = (RelativeLayout) view.findViewById(R.id.rl_bottom_bar);
        if (!this.isCamera) {
            this.mRlDownload.setVisibility(8);
        } else {
            this.mRlDownload.setVisibility(0);
        }
        this.mRlBottomBar.setVisibility(8);
        FontUtil.changeFontLanTing(this.contextWeakReference.get().getAssets(), this.lbBottomDelect, this.mTvBottomDelete, this.mTvBottomDownload);
    }

    protected void initData() {
        setClickEnable(this.lbBottomDelect, false);
        setClickEnable(this.lbBottomDownload, false);
        this.panelRecycleAdapter = new X8sPanelRecycleAdapter<>(this.contextWeakReference.get(), this.isCamera, this);
        if (this.contextWeakReference.get() != null) {
            this.mRecyclerView.setLayoutManager(new MyGridLayoutManager(this.contextWeakReference.get(), 4));
        }
        ((SimpleItemAnimator) this.mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        this.mRecyclerView.setAdapter(this.panelRecycleAdapter);
        this.mRecyclerView.setOverScrollMode(2);
        RBaseItemDecoration mRBaseItemDecoration = new RBaseItemDecoration(this.contextWeakReference.get(), SizeTool.pixToDp(2.5f, this.contextWeakReference.get()), 17170445);
        this.mRecyclerView.addItemDecoration(mRBaseItemDecoration);
        this.mRecyclerView.getItemAnimator().setChangeDuration(0L);
        initPresenter();
    }

    private void initPresenter() {
        if (this.isCamera) {
            this.mBaseFragmentPresenter = new X8CameraFragmentPrensenter(this.mRecyclerView, this.panelRecycleAdapter, this.mISelectData, this.contextWeakReference.get());
        } else {
            this.mBaseFragmentPresenter = new X8LocalFragmentPresenter(this.mRecyclerView, this.panelRecycleAdapter, this.mISelectData, this.contextWeakReference.get());
        }
        this.panelRecycleAdapter.setmIRecycleAdapter(this.mBaseFragmentPresenter);
        if (this.deleteItemReceiver != null) {
            this.deleteItemReceiver.setIReceiver(this.mBaseFragmentPresenter);
        }
    }

    @Override // com.fimi.kernel.base.BaseFragment, android.support.v4.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.contextWeakReference = new WeakReference<>(context);
        if (context instanceof X8MediaActivity) {
            this.mISelectData = (ISelectData) context;
        }
    }

    @Override // android.support.v4.app.Fragment
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

    public void selectFileSize(int size) {
        if (size == 0) {
            if (this.isCamera) {
                setClickEnable(this.lbBottomDelect, false);
                setClickEnable(this.mTvBottomDelete, false);
                setClickEnable(this.lbBottomDownload, false);
                setClickEnable(this.mTvBottomDownload, false);
                return;
            }
            setClickEnable(this.lbBottomDelect, false);
            setClickEnable(this.mTvBottomDelete, false);
        } else if (this.isCamera) {
            setClickEnable(this.lbBottomDelect, true);
            setClickEnable(this.mTvBottomDelete, true);
            setClickEnable(this.lbBottomDownload, true);
            setClickEnable(this.mTvBottomDownload, true);
        } else {
            setClickEnable(this.lbBottomDelect, true);
            setClickEnable(this.mTvBottomDelete, true);
        }
    }

    protected void setClickEnable(View view, boolean isEnable) {
        view.setEnabled(isEnable);
        view.setAlpha(isEnable ? 1.0f : 0.3f);
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
            if (this.mBaseFragmentPresenter.selectList.size() <= 0) {
                setClickEnable(this.lbBottomDownload, false);
                setClickEnable(this.lbBottomDelect, false);
            }
            this.rlMediaSelectBottom.setVisibility(0);
            return;
        }
        this.rlMediaSelectBottom.setVisibility(8);
    }

    @Override // com.fimi.album.iview.INodataTip
    public void noDataTipCallback(boolean isNodata) {
        if (this.rlMediaNoDataTip != null) {
            if (isNodata) {
                this.rlMediaNoDataTip.setVisibility(0);
            } else {
                this.rlMediaNoDataTip.setVisibility(8);
            }
        }
    }

    @Override // com.fimi.album.iview.INodataTip
    public void notifyAddCallback(MediaModel model) {
        this.mBaseFragmentPresenter.notifyAddCallback(model);
    }

    public boolean isModelListEmpty() {
        if (this.mBaseFragmentPresenter == null) {
            return false;
        }
        return this.mBaseFragmentPresenter.isModelListEmpty();
    }

    public void showProgress(boolean isShow) {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(isShow ? 0 : 8);
        }
    }

    /* loaded from: classes.dex */
    public class MyGridLayoutManager extends GridLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        @Override
        // android.support.v7.widget.GridLayoutManager, android.support.v7.widget.LinearLayoutManager, android.support.v7.widget.RecyclerView.LayoutManager
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}

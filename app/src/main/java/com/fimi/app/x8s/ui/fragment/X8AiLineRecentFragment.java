package com.fimi.app.x8s.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.X8AiLineHistoryAdapter2;
import com.fimi.app.x8s.interfaces.IX8AiLineHistoryListener;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.helper.X8AiLinePointInfoHelper;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.map.MapType;

import java.util.List;


public class X8AiLineRecentFragment extends Fragment {
    public static final String ARGS_PAGE = "X8AiLineRecentFragment";
    private List<X8AiLinePointInfo> list;
    private int mPage;
    private RecyclerView mRecyclerView;
    private X8AiLineHistoryAdapter2 mX8AiLineHistoryAdapter2;
    private IX8AiLineHistoryListener mX8AiLineSelectListener;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.x8_fragment_ai_line_history, container, false);
            this.mRecyclerView = this.rootView.findViewById(R.id.recycleview);
            this.list = X8AiLinePointInfoHelper.getIntance().getLastItem(GlobalConfig.getInstance().getMapType() == MapType.AMap ? 1 : 0);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            this.mRecyclerView.setLayoutManager(layoutManager);
            ((SimpleItemAnimator) this.mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            this.mX8AiLineHistoryAdapter2 = new X8AiLineHistoryAdapter2(getContext(), this.list, 0);
            this.mX8AiLineHistoryAdapter2.setOnX8AiLineSelectListener(this.mX8AiLineSelectListener);
            this.mRecyclerView.setAdapter(this.mX8AiLineHistoryAdapter2);
        }
        return this.rootView;
    }

    public void setOnX8AiLineSelectListener(IX8AiLineHistoryListener listener) {
        this.mX8AiLineSelectListener = listener;
    }

    public void notityItemChange(long lineId) {
        if (this.rootView != null) {
            for (int i = 0; i < this.list.size(); i++) {
                if (lineId == this.list.get(i).getId().longValue()) {
                    this.list.get(i).setSaveFlag(0);
                    this.mX8AiLineHistoryAdapter2.notifyItemChanged(i);
                    return;
                }
            }
        }
    }
}

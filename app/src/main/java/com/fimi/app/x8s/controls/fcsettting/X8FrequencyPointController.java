package com.fimi.app.x8s.controls.fcsettting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8FrequencyPointListener;
import com.fimi.app.x8s.widget.X8FrequencyPoint;

import java.util.Random;


public class X8FrequencyPointController extends AbsX8MenuBoxControllers implements View.OnClickListener {
    private View imgRetruen;
    private IX8FrequencyPointListener listener;
    private X8FrequencyPoint vFrePoint;
    private Button[] views;

    public X8FrequencyPointController(View rootView) {
        super(rootView);
    }

    @Override
    public void initViews(View rootView) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.contentView = inflater.inflate(R.layout.x8_main_general_frepoint_setting, (ViewGroup) rootView, true);
        this.views = new Button[5];
        this.imgRetruen = this.contentView.findViewById(R.id.img_return);
        this.vFrePoint = this.contentView.findViewById(R.id.v_fre_point);
        this.views[0] = this.contentView.findViewById(R.id.tv_point1);
        this.views[1] = this.contentView.findViewById(R.id.tv_point2);
        this.views[2] = this.contentView.findViewById(R.id.tv_point3);
        this.views[3] = this.contentView.findViewById(R.id.tv_point4);
        this.views[4] = this.contentView.findViewById(R.id.tv_point5);
        updateUi();
        initActions();
    }

    public void setEnabled(boolean b) {
        for (int i = 0; i < this.views.length; i++) {
            this.views[i].setEnabled(b);
        }
    }

    public void setSelectIndex(int index) {
        for (int i = 0; i < this.views.length; i++) {
            this.views[i].setSelected(i == index - 1);
        }
    }

    public void setSelectDisenable() {
        for (int i = 0; i < this.views.length; i++) {
            this.views[i].setSelected(false);
        }
    }

    @Override
    public void initActions() {
        this.imgRetruen.setOnClickListener(this);
        this.views[0].setOnClickListener(this);
        this.views[1].setOnClickListener(this);
        this.views[2].setOnClickListener(this);
        this.views[3].setOnClickListener(this);
        this.views[4].setOnClickListener(this);
    }

    @Override
    public void onDroneConnected(boolean b) {
        updateUi();
    }

    public void updateUi() {
        getDroneState();
        if (this.isConect) {
            this.vFrePoint.setPercent(new Random().nextInt(100));
            if (this.isInSky) {
                setEnabled(false);
                return;
            } else {
                setEnabled(true);
                return;
            }
        }
        this.vFrePoint.setPercent(0);
        setEnabled(false);
        setSelectDisenable();
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_return) {
            if (this.listener != null) {
                this.listener.onBack();
            }
        } else if (R.id.tv_point1 == id) {
            setSelectIndex(1);
        } else if (R.id.tv_point2 == id) {
            setSelectIndex(2);
        } else if (R.id.tv_point3 == id) {
            setSelectIndex(3);
        } else if (R.id.tv_point4 == id) {
            setSelectIndex(4);
        } else if (R.id.tv_point5 == id) {
            setSelectIndex(5);
        }
    }

    public void setListener(IX8FrequencyPointListener listener) {
        this.listener = listener;
    }
}

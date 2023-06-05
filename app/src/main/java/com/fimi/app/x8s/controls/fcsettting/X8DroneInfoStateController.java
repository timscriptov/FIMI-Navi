package com.fimi.app.x8s.controls.fcsettting;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.X8DroneInfoStateAdapter;
import com.fimi.app.x8s.entity.X8DroneInfoState;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8DroneStateListener;
import com.fimi.app.x8s.manager.X8DroneInfoStatemManager;
import com.fimi.x8sdk.modulestate.StateManager;

import java.util.ArrayList;
import java.util.List;


public class X8DroneInfoStateController extends AbsX8MenuBoxControllers implements X8DroneInfoStateAdapter.OnEventClickListener {
    List<X8DroneInfoState> list;
    private X8DroneInfoStateAdapter adapter;
    private IX8DroneStateListener listener;
    private View parentView;

    public X8DroneInfoStateController(View rootView) {
        super(rootView);
    }

    public IX8DroneStateListener getListener() {
        return this.listener;
    }

    public void setListener(IX8DroneStateListener listener) {
        this.listener = listener;
    }

    @Override
    public void initViews(View rootView) {
        this.list = new ArrayList();
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.parentView = inflater.inflate(R.layout.x8_main_all_setting_drone_info_state, (ViewGroup) rootView, true);
        RecyclerView recyclerView = this.parentView.findViewById(R.id.ryv_drone_state);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        Resources res = rootView.getContext().getResources();
        String[] states = res.getStringArray(R.array.x8_drone_info_state_array);
        for (int i = 0; i < states.length; i++) {
            X8DroneInfoState state = new X8DroneInfoState();
            state.setName(states[i]);
            state.setState(State.NA);
            state.setMode(getMode(i));
            state.setInfo(res.getString(R.string.x8_na));
            state.setErrorEvent(getEvent(state.getMode()));
            this.list.add(state);
        }
        this.adapter = new X8DroneInfoStateAdapter(this.list);
        this.adapter.setOnEventClickListener(this);
        recyclerView.setAdapter(this.adapter);
        initActions();
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    public Mode getMode(int index) {
        Mode mode = Mode.GPS;
        switch (index) {
            case 0:
                Mode mode2 = Mode.GPS;
                return mode2;
            case 1:
                Mode mode3 = Mode.CAMP;
                return mode3;
            case 2:
                Mode mode4 = Mode.MAGNETIC;
                return mode4;
            case 3:
                Mode mode5 = Mode.IMU;
                return mode5;
            case 4:
                Mode mode6 = Mode.BATTERY;
                return mode6;
            case 5:
                Mode mode7 = Mode.GIMBAL;
                return mode7;
            default:
                return mode;
        }
    }

    public boolean checkError(Mode mode) {
        switch (mode) {
            case GPS:
                boolean ret = X8DroneInfoStatemManager.isGpsError();
                return ret;
            case CAMP:
                boolean ret2 = X8DroneInfoStatemManager.isCompassError();
                return ret2;
            case MAGNETIC:
                boolean ret3 = StateManager.getInstance().getErrorCodeState().isMagneticError();
                return ret3;
            case IMU:
                boolean ret4 = X8DroneInfoStatemManager.isImuError();
                return ret4;
            case BATTERY:
                boolean ret5 = X8DroneInfoStatemManager.isBatteryError();
                return ret5;
            case GIMBAL:
                boolean ret6 = X8DroneInfoStatemManager.isGcError();
                return ret6;
            default:
                return false;
        }
    }

    public String getInfo(Mode mode) {
        switch (mode) {
            case GPS:
                String s = getString(R.string.x8_fc_state_exception);
                return s;
            case CAMP:
                String s2 = getString(R.string.x8_fc_state_exception);
                return s2;
            case MAGNETIC:
                int magnetic = StateManager.getInstance().getX8Drone().getFcSingal().getMagnetic();
                if (magnetic >= 0 && magnetic <= 20) {
                    String s3 = getString(R.string.x8_fc_item_magnetic_field_error1);
                    return s3;
                } else if (magnetic >= 21 && magnetic <= 40) {
                    String s4 = getString(R.string.x8_fc_item_magnetic_field_error2);
                    return s4;
                } else {
                    String s5 = getString(R.string.x8_fc_item_magnetic_field_error3);
                    return s5;
                }
            case IMU:
                String s6 = getString(R.string.x8_fc_state_exception);
                return s6;
            case BATTERY:
                String s7 = getString(R.string.x8_fc_state_exception);
                return s7;
            case GIMBAL:
                String s8 = getString(R.string.x8_fc_state_exception);
                return s8;
            default:
                return "";
        }
    }

    public int getEvent(Mode mode) {
        if (mode == Mode.CAMP) {
            return 1;
        }
        return 0;
    }

    @Override
    public void onItemClick(int index, X8DroneInfoState obj) {
        if (obj.getMode() == Mode.CAMP) {
            this.listener.onCalibrationItemClick();
            return;
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (b) {
            for (X8DroneInfoState state : this.list) {
                boolean error = checkError(state.getMode());
                if (error) {
                    state.setInfo(getInfo(state.getMode()));
                    state.setState(State.ERROR);
                } else {
                    String s = getString(R.string.x8_fc_state_normal);
                    State st = State.NORMAL;
                    if (Mode.MAGNETIC == state.getMode()) {
                        int magnetic = StateManager.getInstance().getX8Drone().getFcSingal().getMagnetic();
                        if (magnetic >= 0 && magnetic <= 20) {
                            s = getString(R.string.x8_fc_item_magnetic_field_error1);
                        } else if (magnetic >= 21 && magnetic <= 40) {
                            s = getString(R.string.x8_fc_item_magnetic_field_error2);
                            st = State.MIDDLE;
                        } else {
                            s = getString(R.string.x8_fc_item_magnetic_field_error3);
                            st = State.ERROR;
                        }
                    }
                    state.setInfo(s);
                    state.setState(st);
                    this.adapter.notifyDataSetChanged();
                }
                this.adapter.notifyDataSetChanged();
            }
            return;
        }
        for (X8DroneInfoState state2 : this.list) {
            state2.setState(State.NA);
            state2.setInfo(getString(R.string.x8_na));
            this.adapter.notifyDataSetChanged();
        }
    }


    public enum Mode {
        GPS,
        CAMP,
        MAGNETIC,
        IMU,
        BATTERY,
        GIMBAL
    }


    public enum State {
        NA,
        NORMAL,
        MIDDLE,
        ERROR
    }
}

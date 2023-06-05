package com.fimi.app.x8s.map.interfaces;

import android.os.Bundle;
import android.view.View;

import com.fimi.app.x8s.enums.NoFlyZoneEnum;
import com.fimi.x8sdk.dataparser.AckNoFlyNormal;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;

/* loaded from: classes.dex */
public interface IFimiMap {
    public static final String FIMI_GAODE_MAP = "fimi.gaode.map";

    void changeGoogleCamera(CameraUpdate cameraUpdate);

    void clearNoFlightZone();

    void drawNoFlightZone(AckNoFlyNormal ackNoFlyNormal, NoFlyZoneEnum noFlyZoneEnum);

    View getMapView();

    float getZoom();

    GoogleMap googleMap();

    void onCreate(View view, Bundle bundle);

    void onDestroy();

    void onPause();

    void onResume();

    void onSaveInstanceState(Bundle bundle);

    void switchMapStyle(int i);
}

package com.fimi.app.x8s.map;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.fimi.app.x8s.enums.NoFlyZoneEnum;
import com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady;
import com.fimi.app.x8s.interfaces.IX8AiItemMapListener;
import com.fimi.app.x8s.map.interfaces.AbsAiLineManager;
import com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager;
import com.fimi.app.x8s.map.interfaces.AbsAiSurroundManager;
import com.fimi.app.x8s.map.interfaces.AbsFimiMap;
import com.fimi.app.x8s.map.manager.google.GglMapAiLineManager;
import com.fimi.app.x8s.map.manager.google.GglMapAiPoint2PointManager;
import com.fimi.app.x8s.map.manager.google.GglMapAiSurroundManager;
import com.fimi.app.x8s.map.manager.google.GglMapLocationManager;
import com.fimi.app.x8s.map.model.FimiPoint;
import com.fimi.app.x8s.map.model.GglMapNoFlyZone;
import com.fimi.app.x8s.map.model.GoogleMapPoint;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.dataparser.AckNoFlyNormal;
import com.fimi.x8sdk.dataparser.AutoHomeInfo;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class GglMap extends AbsFimiMap implements OnMapReadyCallback {
    LatLng BEIJING = new LatLng(22.63916666d, 113.8108333d);
    private GglMapAiLineManager aiLineManager;
    private GglMapAiPoint2PointManager aiP2PManager;
    private GglMapAiSurroundManager aiSurroundManager;
    private GglMapLocationManager gglMapLocationManager;
    private GoogleMap googleMap;
    private GoogleMapPoint googleMapPoint;
    private boolean isInit;
    private IX8AiItemMapListener mX8AiItemMapListener;
    private MapView mapView;
    private GglMapNoFlyZone noFlyZone;
    private float currentZoom = 0.0f;

    @Override
    public void setmX8AiItemMapListener(IX8AiItemMapListener mX8AiItemMapListener) {
        this.mX8AiItemMapListener = mX8AiItemMapListener;
    }

    @Override
    public boolean isMapInit() {
        return this.isInit;
    }

    @Override
    public boolean hasHomeInfo() {
        return this.isInit && this.gglMapLocationManager != null && this.gglMapLocationManager.getHomeLocation() != null;
    }

    @Override
    public float getAccuracy() {
        if (!this.isInit || this.gglMapLocationManager == null) {
            return 0.0f;
        }
        return this.gglMapLocationManager.getAccuracy();
    }

    @Override
    public View getMapView() {
        return this.mapView;
    }

    @Override
    public GoogleMap googleMap() {
        return this.googleMap;
    }

    @Override
    public void setHomeLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        addHomeLocation(latLng);
    }

    @Override
    public void onCreate(@NonNull View rootView, Bundle savedInstanceState) {
        this.mapView = new MapView(rootView.getContext());
        this.mapView.onCreate(savedInstanceState);
        this.mapView.setEnabled(true);
        this.mapView.setClickable(true);
        this.mapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        this.mapView.onResume();
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.onStart();
        }
    }

    @Override
    public void onPause() {
        this.mapView.onPause();
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.onStop();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        this.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        this.mapView.onDestroy();
    }

    @Override
    public float getZoom() {
        float f = this.googleMap.getCameraPosition().zoom;
        this.currentZoom = f;
        return f;
    }

    @Override
    public void switchMapStyle(int mapStyle) {
        if (this.isInit) {
            if (mapStyle == Constants.X8_GENERAL_MAP_STYLE_NORMAL) {
                GoogleMap googleMap = this.googleMap;
                googleMap.setMapType(1);
            } else if (mapStyle == Constants.X8_GENERAL_MAP_STYLE_SATELLITE) {
                GoogleMap googleMap = this.googleMap;
                googleMap.setMapType(2);
            }
        }
    }

    @Override
    public void changeGoogleCamera(CameraUpdate update) {
        this.googleMap.moveCamera(update);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.noFlyZone = new GglMapNoFlyZone(googleMap);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.BEIJING, 21.0f));
        if (ActivityCompat.checkSelfPermission(this.context, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this.context, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            this.gglMapLocationManager = new GglMapLocationManager(googleMap, this.context);
            this.aiP2PManager = new GglMapAiPoint2PointManager(this.context, googleMap, this.gglMapLocationManager);
            this.aiSurroundManager = new GglMapAiSurroundManager(this.context, googleMap, this.gglMapLocationManager);
            this.aiLineManager = new GglMapAiLineManager(this.context, googleMap, this.gglMapLocationManager);
            this.gglMapLocationManager.onStart();
            this.isInit = true;
            if (GlobalConfig.getInstance().getMapStyle() == Constants.X8_GENERAL_MAP_STYLE_NORMAL) {
                googleMap.setMapType(1);
            } else if (GlobalConfig.getInstance().getMapStyle() == Constants.X8_GENERAL_MAP_STYLE_SATELLITE) {
                googleMap.setMapType(2);
            }
        }
    }

    @Override
    public void onSensorChanged(float degree) {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.onSensorChanged(degree);
        }
    }

    @Override
    public void addDeviceLocation(double latitude, double logitude) {
        LatLng mLatLng = new LatLng(latitude, logitude);
        addDeviceLocation(mLatLng);
        this.gglMapLocationManager.addDeviceLocation(mLatLng);
        this.gglMapLocationManager.drawFlyLine();
    }

    @Override
    public void chaneDeviceAngle(float angle) {
        this.gglMapLocationManager.chaneDeviceAngle(angle);
    }

    @Override
    public void addFlyPolyline(double latitude, double logitude) {
        DroneState droneState = StateManager.getInstance().getX8Drone();
        if (droneState.isInSky()) {
            this.gglMapLocationManager.addFlyPolyLine(latitude, logitude);
        } else {
            this.gglMapLocationManager.clearFlyPolyLine();
        }
    }

    @Override
    public void defaultMapValue() {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.clearMarker();
        }
    }

    @Override
    public void animateCamer() {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.animatePersonLocation();
        }
    }

    @Override
    public AbsAiLineManager getAiLineManager() {
        return this.aiLineManager;
    }

    @Override
    public AbsAiPoint2PointManager getAiPoint2PointManager() {
        return this.aiP2PManager;
    }

    @Override
    public AbsAiSurroundManager getAiSurroundManager() {
        return this.aiSurroundManager;
    }

    @Override
    public FimiPoint toScreenLocation(double lat, double lng) {
        FimiPoint p = new FimiPoint();
        LatLng mLatlng = new LatLng(lat, lng);
        Point mPoint = this.googleMap.getProjection().toScreenLocation(mLatlng);
        if (mPoint != null) {
            p.x = mPoint.x;
            p.y = mPoint.y;
        }
        return p;
    }

    @Override
    public MapPointLatLng getDeviceLatlng() {
        MapPointLatLng p = new MapPointLatLng();
        LatLng latlng = this.gglMapLocationManager.getDevLocation();
        if (latlng != null) {
            p.latitude = latlng.latitude;
            p.longitude = latlng.longitude;
        }
        return p;
    }

    @Override
    public void drawNoFlightZone(AckNoFlyNormal flyNormal, NoFlyZoneEnum noFlyZoneEnum) {
        if (this.noFlyZone != null) {
            if (this.googleMapPoint == null) {
                this.googleMapPoint = new GoogleMapPoint();
                this.googleMapPoint.setCenter(new LatLng(flyNormal.getCenter().latitude, flyNormal.getCenter().longitude));
                if (flyNormal.getPolygonShape() == 2) {
                    this.googleMapPoint.setA1(new LatLng(flyNormal.getA1().latitude, flyNormal.getA1().longitude));
                    this.googleMapPoint.setA2(new LatLng(flyNormal.getA2().latitude, flyNormal.getA2().longitude));
                    this.googleMapPoint.setB1(new LatLng(flyNormal.getB1().latitude, flyNormal.getB1().longitude));
                    this.googleMapPoint.setB2(new LatLng(flyNormal.getB2().latitude, flyNormal.getB2().longitude));
                    this.googleMapPoint.setC1(new LatLng(flyNormal.getC1().latitude, flyNormal.getC1().longitude));
                    this.googleMapPoint.setC2(new LatLng(flyNormal.getC2().latitude, flyNormal.getC2().longitude));
                    this.googleMapPoint.setD1(new LatLng(flyNormal.getD1().latitude, flyNormal.getD1().longitude));
                    this.googleMapPoint.setD2(new LatLng(flyNormal.getD2().latitude, flyNormal.getD2().longitude));
                } else if (flyNormal.getPolygonShape() == 0) {
                    this.googleMapPoint.setRadius(flyNormal.getNoflyRadius());
                } else if (flyNormal.getNumEudges() > 0 && flyNormal.getPolygonShape() == 3) {
                    for (int i = 0; i < flyNormal.getNumEudges(); i++) {
                        LatLng latLng = new LatLng(flyNormal.getPoints().get(i).latitude, flyNormal.getPoints().get(i).longitude);
                        this.googleMapPoint.getLatLngs().add(new LatLng(latLng.latitude, latLng.longitude));
                    }
                }
                this.googleMapPoint.setNfzType(flyNormal.getPolygonShape());
                this.googleMapPoint.setLimitHight(flyNormal.getHeightLimit());
                this.googleMapPoint.setRadius(flyNormal.getNoflyRadius());
                this.googleMapPoint.getLatLngs().clear();
                this.googleMapPoint.setType(noFlyZoneEnum);
            }
            LatLng center = this.googleMapPoint.getCenter();
            LatLng A1 = this.googleMapPoint.getA1();
            LatLng A2 = this.googleMapPoint.getA2();
            LatLng C1 = this.googleMapPoint.getC1();
            LatLng C2 = this.googleMapPoint.getC2();
            LatLng B1 = this.googleMapPoint.getB1();
            LatLng B2 = this.googleMapPoint.getB2();
            LatLng D1 = this.googleMapPoint.getD1();
            LatLng D2 = this.googleMapPoint.getD2();
            if (this.googleMapPoint.getType() == NoFlyZoneEnum.CANDY) {
                this.noFlyZone.drawCandyNoFlyZone(new LatLng[]{center, D1, B1, C1, A1, A2, C2, B2, D2});
            } else if (this.googleMapPoint.getType() == NoFlyZoneEnum.CIRCLE) {
                this.noFlyZone.drawCircleNoFlyZone(center, this.googleMapPoint.getRadius());
            } else if (this.googleMapPoint.getType() == NoFlyZoneEnum.IRREGULAR) {
                List<LatLng> list = this.googleMapPoint.getLatLngs();
                LatLng[] lats = new LatLng[list.size()];
                list.toArray(lats);
                this.noFlyZone.drawIrregularNoFlyZone(lats, this.googleMapPoint.isNoFly());
            }
        }
    }

    @Override
    public void clearNoFlightZone() {
        if (this.noFlyZone != null) {
            this.noFlyZone.clearNoFlightZone();
        }
    }

    public void addHomeLocation(LatLng latLng) {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.addHomeLocation(latLng);
        }
        if (this.mX8AiItemMapListener != null) {
            switch (this.mX8AiItemMapListener.getCurrentItem()) {
                case AI_POINT_TO_POINT:
                    this.aiP2PManager.drawAiLimit(latLng.latitude, latLng.longitude, 1000.0d);
                    return;
                case AI_LINE:
                    this.aiLineManager.drawAiLimit(latLng.latitude, latLng.longitude, 1000.0d);
                    return;
                case AI_SURROUND:
                    this.aiSurroundManager.drawAiLimit(latLng.latitude, latLng.longitude, 1000.0d);
                    return;
                default:
            }
        }
    }

    public void addDeviceLocation(LatLng latLng) {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.addDeviceLocation(latLng);
            this.gglMapLocationManager.drawFlyLine();
        }
        if (this.aiLineManager != null) {
            this.aiLineManager.changeLine();
        }
        if (this.aiP2PManager != null) {
            this.aiP2PManager.changeLine();
        }
    }

    @Override
    public void moveCameraByDevice() {
        if (this.gglMapLocationManager != null) {
            this.gglMapLocationManager.moveCameraByDevice();
        }
    }

    @Override
    public double[] getDevicePosition() {
        if (this.gglMapLocationManager != null) {
            return this.gglMapLocationManager.getDevicePosition();
        }
        return null;
    }

    @Override
    public void onLocationEvnent() {
        AutoHomeInfo homeInfo = StateManager.getInstance().getX8Drone().getHomeInfo();
        LatLng latLng = new LatLng(homeInfo.getFLatLng().latitude, homeInfo.getFLatLng().longitude);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
    }

    @Override
    public void onLocationGravitTrailEvent() {
        LatLng latLng = new LatLng(StateManager.getInstance().getX8Drone().getLatitude(), StateManager.getInstance().getX8Drone().getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20.0f));
    }

    @Override
    public double[] getManLatLng() {
        LatLng ll;
        double[] latLng = new double[2];
        if (this.gglMapLocationManager != null && (ll = this.gglMapLocationManager.getManLocation()) != null) {
            latLng[0] = ll.latitude;
            latLng[1] = ll.longitude;
            return latLng;
        }
        return null;
    }

    @Override
    public void snapshot(@NonNull final IFimiOnSnapshotReady callBack) {
        this.googleMap.snapshot(callBack::onSnapshotReady);
    }
}

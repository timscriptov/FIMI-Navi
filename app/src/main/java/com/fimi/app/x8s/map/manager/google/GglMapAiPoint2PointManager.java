package com.fimi.app.x8s.map.manager.google;

import android.content.Context;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.google.GglMapCustomMarkerView;
import com.fimi.app.x8s.tools.GeoTools;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.util.GpsCorrect;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class GglMapAiPoint2PointManager extends AbsAiPoint2PointManager implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    boolean isFollow;
    List<LatLng> latLngs = new ArrayList();
    private Context context;
    private GglMapLocationManager gglMapLocationManager;
    private GoogleMap googleMap;
    private Circle limitCircle;
    private MapPointLatLng mp;
    private IX8MarkerListener point2PointMarkerSelectListener;
    private Marker pointMarker;
    private Polyline polyline;

    public GglMapAiPoint2PointManager(Context context, GoogleMap googleMap, GglMapLocationManager gglMapLocationManager) {
        this.context = context;
        this.googleMap = googleMap;
        this.gglMapLocationManager = gglMapLocationManager;
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsBaseManager
    public void setMarkerViewInfo(float height) {
        int res;
        if (this.pointMarker != null) {
            this.mp.altitude = height;
            GglMapCustomMarkerView gdCustemMarkerView = new GglMapCustomMarkerView();
            if (this.mp.isSelect) {
                res = R.drawable.x8_img_ai_follow_point;
            } else {
                res = R.drawable.x8_img_ai_follow_point2;
            }
            BitmapDescriptor mBitmapDescriptor = gdCustemMarkerView.createCustomMarkerViewForP2P(this.context, res, this.mp.altitude, this.mp.nPos);
            this.pointMarker.setIcon(mBitmapDescriptor);
        }
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsBaseManager
    public float getLineAngleByMapBealing(float angle) {
        return 0.0f;
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsBaseManager
    public void setOnMapClickListener() {
        setOnMarkerListener();
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsBaseManager
    public void removeMapClickListener() {
        this.googleMap.setOnMapClickListener(null);
        this.googleMap.setOnMarkerClickListener(null);
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsBaseManager
    public void resetMapEvent() {
        this.googleMap.setOnMapClickListener(null);
        this.googleMap.setOnMarkerClickListener(null);
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsBaseManager
    public void drawAiLimit(double lat, double lng, double radiu) {
        if (this.limitCircle == null) {
            CircleOptions circle = new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokeColor(this.lineLimitColor).fillColor(this.fillColor).strokeWidth(this.strokeWidth);
            this.limitCircle = this.googleMap.addCircle(circle);
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager
    public MapPointLatLng getMapPointLatLng() {
        if (this.pointMarker != null) {
            FLatLng mFlatlng = GpsCorrect.Mars_To_Earth0(this.pointMarker.getPosition().latitude, this.pointMarker.getPosition().longitude);
            this.mp.longitude = mFlatlng.longitude;
            this.mp.latitude = mFlatlng.latitude;
        }
        return this.mp;
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager
    public void setPoint2PointMarkerSelectListener(IX8MarkerListener listener) {
        this.point2PointMarkerSelectListener = listener;
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager
    public void clearPoint2PointMarker() {
        clearPointMark();
        this.isFollow = false;
    }

    private void clearPointMark() {
        if (this.pointMarker != null) {
            this.pointMarker.remove();
            this.pointMarker = null;
        }
        clearMarker();
    }

    public void clearMarker() {
        if (this.pointMarker != null) {
            this.pointMarker.remove();
            this.pointMarker = null;
        }
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        if (this.polyline != null) {
            this.polyline.remove();
            this.polyline = null;
        }
        if (this.latLngs != null) {
            this.latLngs.clear();
        }
        this.mp = null;
        this.gglMapLocationManager.clearFlyPolyLine();
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager
    public void setMarkerByDevice(double latitude, double logitude, int altitude) {
        if (this.gglMapLocationManager.getHomeLocation() != null) {
            LatLng latlng = new LatLng(latitude, logitude);
            addPointLatLng(latlng, 0.0f, this.gglMapLocationManager.getDevLocation());
            setMarkerViewInfo(altitude / 10.0f);
        }
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager
    public void changeLine() {
        if (this.isFollow && this.polyline != null) {
            changeDeviceLocation(this.gglMapLocationManager.getDevLocation());
        }
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager
    public void setRunning() {
        this.isFollow = false;
    }

    private void setOnMarkerListener() {
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMapClickListener
    public void onMapClick(LatLng latLng) {
        onMapClickForAiP2P(latLng);
    }

    public void onMapClickForAiP2P(LatLng latLng) {
        if (this.gglMapLocationManager.getHomeLocation() != null) {
            LatLng d = this.gglMapLocationManager.getHomeLocation();
            float distance = (float) GeoTools.getDistance(latLng, d).valueInMeters();
            if (0.0f <= distance && distance <= 1000.0f) {
                addPointLatLng(latLng, distance, this.gglMapLocationManager.getDevLocation());
            } else if (distance > 1000.0f) {
                String t = String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), X8NumberUtil.getDistanceNumberString(1000.0f, 0, true));
                ToastUtil.showToast(this.context, t, 0);
            }
        }
    }

    @Override // com.fimi.app.x8s.map.interfaces.AbsAiPoint2PointManager
    public void calcDistance() {
        if (this.pointMarker != null) {
            LatLng des = this.gglMapLocationManager.getDevLocation();
            float distanceDes = (float) GeoTools.getDistance(this.pointMarker.getPosition(), des).valueInMeters();
            this.mp.distance = distanceDes;
        }
    }

    public void addPointLatLng(LatLng latLng, float distance, LatLng deviceLocation) {
        if (this.pointMarker == null) {
            this.mp = new MapPointLatLng();
            this.mp.altitude = 5.0f;
            DroneState droneState = StateManager.getInstance().getX8Drone();
            if (droneState.isConnect()) {
                float alt = StateManager.getInstance().getX8Drone().getHeight();
                int h = Math.round(alt);
                if (h > 5) {
                    this.mp.altitude = h;
                }
            }
            GglMapCustomMarkerView gdCustemMarkerView = new GglMapCustomMarkerView();
            BitmapDescriptor mBitmapDescriptor = gdCustemMarkerView.createCustomMarkerViewForP2P(this.context, R.drawable.x8_img_ai_follow_point2, this.mp.altitude, this.mp.nPos);
            this.pointMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.9f).draggable(false));
            this.pointMarker.setDraggable(true);
            this.pointMarker.setTag(this.mp);
        } else {
            this.pointMarker.setPosition(latLng);
        }
        drawPointLine(deviceLocation);
        this.mp.distance = distance;
        if (this.point2PointMarkerSelectListener != null) {
            this.point2PointMarkerSelectListener.onMarkerSelect(true, this.mp.altitude, this.mp, false);
        }
        this.isFollow = true;
    }

    public void drawPointLine(LatLng latLngDevice) {
        if (this.pointMarker != null) {
            LatLng latLng = this.pointMarker.getPosition();
            this.latLngs.clear();
            this.latLngs.add(latLng);
            this.latLngs.add(latLngDevice);
            if (this.polyline == null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(this.latLngs);
                polylineOptions.color(this.context.getResources().getColor(R.color.x8_drone_inface_line)).zIndex(50.0f);
                polylineOptions.width(4.0f);
                if (this.polyline != null) {
                    this.polyline.remove();
                }
                this.polyline = this.googleMap.addPolyline(polylineOptions);
                this.polyline.setPattern(PATTERN_DASHED);
            }
            this.polyline.setPoints(this.latLngs);
        }
    }

    public void changeDeviceLocation(LatLng latLngDevice) {
        drawPointLine(latLngDevice);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}

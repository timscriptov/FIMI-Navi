package com.fimi.app.x8s.controls.fcsettting.flightlog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.GoogleMapSyncReady;
import com.fimi.app.x8s.interfaces.UpdateChangeMapTypeInterface;
import com.fimi.kernel.Constants;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcSportStatePlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoHomeInfoPlayback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class PlayBackMapFragment extends SupportMapFragment implements GoogleMap.InfoWindowAdapter, GoogleMapSyncReady, OnMapReadyCallback {
    private static final PatternItem DASH = new Dash(20.0f);
    private static final PatternItem GAP = new Gap(20.0f);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DASH);
    protected GoogleMap mMap;
    private Circle circleb;
    private Circle circlemid;
    private Bitmap.Config conf;
    private Marker droneMarker;
    private Polyline dronePolyline;
    private Marker homeMarker;
    private boolean isChangeCamera = true;
    private final List<Circle> listCircle = new CopyOnWriteArrayList();
    private volatile List<LatLng> listDronePoint;
    private UpdateChangeMapTypeInterface mUpdateChangeMapTypeInterface;
    private SharedPreferences sharepre;
    private Marker textMarker;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mUpdateChangeMapTypeInterface = (UpdateChangeMapTypeInterface) activity;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.sharepre = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View view = super.onCreateView(inflater, viewGroup, bundle);
        getMapAsync(this);
        return view;
    }

    private void initMap() {
        this.sharepre.edit().putInt(Constants.DRONEMAPTYPE, 1).commit();
        this.mMap.setMapType(1);
        this.conf = Bitmap.Config.ARGB_8888;
        if (this.mUpdateChangeMapTypeInterface != null) {
            this.mUpdateChangeMapTypeInterface.update(this.mMap.getMapType());
        }
        this.mMap.getUiSettings().setCompassEnabled(false);
        this.mMap.setTrafficEnabled(false);
        UiSettings mUiSettings = this.mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (PlayBackMapFragment.this.textMarker != null) {
                    PlayBackMapFragment.this.textMarker.showInfoWindow();
                }
            }
        });
        this.mMap.setInfoWindowAdapter(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public int changeMapType() {
        int valuetype;
        if (this.sharepre.getInt(Constants.DRONEMAPTYPE, 1) == 1) {
            this.sharepre.edit().putInt(Constants.DRONEMAPTYPE, 2).commit();
            valuetype = 2;
        } else {
            this.sharepre.edit().putInt(Constants.DRONEMAPTYPE, 1).commit();
            valuetype = 1;
        }
        if (this.mMap != null) {
            this.mMap.setMapType(valuetype);
        }
        return valuetype;
    }

    @Override
    public void onDestroy() {
        if (this.mUpdateChangeMapTypeInterface != null) {
            this.mUpdateChangeMapTypeInterface = null;
        }
        if (this.homeMarker != null) {
            this.homeMarker.remove();
            this.homeMarker = null;
        }
        if (this.droneMarker != null) {
            this.droneMarker.remove();
            this.droneMarker = null;
        }
        if (this.dronePolyline != null) {
            this.dronePolyline.remove();
            this.dronePolyline = null;
        }
        if (this.circleb != null) {
            this.circleb.remove();
            this.circleb = null;
        }
        if (this.circlemid != null) {
            this.circlemid.remove();
            this.circlemid = null;
        }
        this.listCircle.clear();
        super.onDestroy();
    }

    public void handlerHomePoint(AutoHomeInfoPlayback autoHomeInfoPlayback) {
        LatLng latLng = new LatLng(autoHomeInfoPlayback.getHomeLatitude(), autoHomeInfoPlayback.getHomeLongitude());
        if (this.homeMarker == null) {
            if (this.mMap != null) {
                this.homeMarker = this.mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.home_point)).position(latLng));
                return;
            }
            return;
        }
        this.homeMarker.setPosition(latLng);
    }

    public void handlerDroneMarker(AutoFcSportStatePlayback autoFcSportStatePlayback) {
        LatLng latLng = new LatLng(autoFcSportStatePlayback.getDeviceLatitude(), autoFcSportStatePlayback.getDeviceLongitude());
        List<LatLng> listDronePoint = getListDronePoint();
        if (!listDronePoint.contains(latLng)) {
            listDronePoint.add(latLng);
        }
        if (this.droneMarker == null) {
            this.droneMarker = createDroneMarker(latLng);
            if (this.droneMarker != null) {
                this.droneMarker.setAnchor(0.5f, 0.5f);
            }
        } else {
            changeDroneMarkerAngle(autoFcSportStatePlayback.getDeviceAngle());
            this.droneMarker.setPosition(latLng);
        }
        String limitLatitude = FormatValueEightPoint(latLng.latitude);
        String limitLongitude = FormatValueEightPoint(latLng.longitude);
        if (this.textMarker == null) {
            this.textMarker = addTextToGoogleMap(latLng);
        } else {
            this.textMarker.setPosition(latLng);
            this.textMarker.setTitle(limitLatitude + "，" + limitLongitude);
            this.textMarker.showInfoWindow();
        }
        changePolyLineList(listDronePoint);
    }

    public void changeDroneMarkerAngle(float angle) {
        if (this.droneMarker != null) {
            float currentMapAngle = bearAngle();
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            this.droneMarker.setRotation(angle - currentMapAngle);
        }
    }

    private synchronized float bearAngle() {
        CameraPosition cameraPosition;
        float f = 0.0f;
        synchronized (this) {
            try {
                if (this.mMap != null && (cameraPosition = this.mMap.getCameraPosition()) != null) {
                    f = cameraPosition.bearing;
                }
            } catch (Exception e) {
            }
        }
        return f;
    }

    public void moveDroneMarker(LatLng latLng, List<LatLng> listDronePoint) {
        if (latLng != null && listDronePoint != null) {
            if (this.droneMarker != null) {
                this.droneMarker.setPosition(latLng);
            } else {
                this.droneMarker = createDroneMarker(latLng);
                this.droneMarker.setAnchor(0.5f, 0.5f);
            }
            if (this.droneMarker != null) {
                String limitLatitude = FormatValueEightPoint(latLng.latitude);
                String limitLongitude = FormatValueEightPoint(latLng.longitude);
                this.droneMarker.setTitle(limitLatitude + "，" + limitLongitude);
                this.droneMarker.showInfoWindow();
            }
            changePolyLineList(listDronePoint);
        }
    }

    public void changePolyLineList(List<LatLng> list) {
        if (list != null) {
            if (this.dronePolyline == null) {
                this.dronePolyline = createDronePolyline(list);
            } else {
                this.dronePolyline.setPoints(list);
            }
            if (this.isChangeCamera && this.mMap != null) {
                setChangeCamera(false);
                this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 18.0f));
            }
        }
    }

    private Marker createDroneMarker(LatLng latLng) {
        if (this.mMap == null) {
            return null;
        }
        return this.mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_fly_handpiece_location)).position(latLng));
    }

    private Polyline createDronePolyline(List<LatLng> list) {
        if (this.mMap == null) {
            return null;
        }
        PolylineOptions polylineOptions = new PolylineOptions().addAll(list).color(Color.rgb(242, 188, 13)).width(3.0f);
        polylineOptions.pattern(PATTERN_POLYLINE_DOTTED);
        return this.mMap.addPolyline(polylineOptions);
    }

    public void clearAll() {
        if (this.homeMarker != null) {
            this.homeMarker.remove();
            this.homeMarker = null;
        }
        if (this.droneMarker != null) {
            this.droneMarker.remove();
            this.droneMarker = null;
        }
        if (this.dronePolyline != null) {
            this.dronePolyline.remove();
            this.dronePolyline = null;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.play_back_inforwindow, null);
        TextView droneLocation = view.findViewById(R.id.drone_location);
        droneLocation.setText(marker.getTitle());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public boolean isChangeCamera() {
        return this.isChangeCamera;
    }

    public void setChangeCamera(boolean changeCamera) {
        this.isChangeCamera = changeCamera;
    }

    private void createRadius(LatLng position, int radius, int flyzonetype) {
        int type = this.sharepre.getInt(Constants.AMPTYPE, 1);
        if (flyzonetype == 3) {
            if (this.circlemid != null) {
                this.circlemid.setCenter(position);
                if (radius != this.circlemid.getRadius()) {
                    this.circlemid.setRadius(radius + 100);
                }
            } else if (this.mMap != null) {
                this.circlemid = this.mMap.addCircle(new CircleOptions().center(position).radius(radius + 100).strokeColor(Color.argb(127, 0, 0, 0)).strokeWidth(2.0f).fillColor(1 == type ? Color.argb(204, 198, 200, 203) : Color.argb(204, 101, 104, 106)));
            }
        } else if (this.circlemid != null) {
            this.circlemid.remove();
            this.circlemid = null;
        }
        if (this.circleb != null && this.listCircle.contains(this.circleb)) {
            this.circleb.setCenter(position);
            if (radius != this.circleb.getRadius()) {
                this.circleb.setRadius(radius);
            }
        } else if (this.mMap != null) {
            this.circleb = this.mMap.addCircle(new CircleOptions().center(position).radius(radius).strokeColor(Color.argb(127, 255, 54, 0)).strokeWidth(2.0f).fillColor(Color.argb(51, 255, 54, 0)).zIndex(100.0f));
        }
        if (!this.listCircle.contains(this.circleb)) {
            this.listCircle.add(this.circleb);
        }
    }

    @Override
    public void isReady(GoogleMap googleMap) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.mMap = googleMap;
            initMap();
        }
    }

    private synchronized String FormatValueEightPoint(double value) {
        return String.format("%.8f", Double.valueOf(value));
    }

    public List<LatLng> getListDronePoint() {
        if (this.listDronePoint == null) {
            this.listDronePoint = new LinkedList();
        }
        return this.listDronePoint;
    }

    private Marker addTextToGoogleMap(LatLng latLng) {
        if (this.mMap == null) {
            return null;
        }
        Bitmap bmpText = Bitmap.createBitmap(20, 20, this.conf);
        return this.mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bmpText)).position(latLng).anchor(0.5f, 4.0f));
    }
}

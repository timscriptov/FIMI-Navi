package com.fimi.app.x8s.map.manager.google;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.fimi.android.app.R;
import com.fimi.app.x8s.entity.X8PressureGpsInfo;
import com.fimi.app.x8s.manager.X8MapGetCityManager;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.x8sdk.modulestate.TimeStampState;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.VisibleRegion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GglMapLocationManager extends LocationCallback implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final Context context;
    private final LocationRequest locationRequest;
    private final MarkerOptions markerOptions;
    private final List<LatLng> latLngs = new ArrayList();
    Marker locationMarker;
    GoogleApiClient mGoogleApiClient;
    GoogleMap mGoogleMap;
    private float accuracy;
    private Marker deviceMarker;
    private Polyline flyPolyLine;
    private Marker home;
    private int state = 0;

    @SuppressLint({"RestrictedApi"})
    public GglMapLocationManager(GoogleMap googleMap, Context context) {
        this.mGoogleMap = googleMap;
        this.context = context;
        buildGoogleApiClient();
        this.markerOptions = new MarkerOptions();
        this.markerOptions.title("Current Position");
        this.markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map));
        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(1000L);
        this.locationRequest.setFastestInterval(1000L);
        this.locationRequest.setPriority(100);
    }

    public void onStop() {
        if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, this);
            this.mGoogleApiClient.disconnect();
        }
    }

    public void onStart() {
        if (this.mGoogleApiClient != null && !this.mGoogleApiClient.isConnected()) {
            this.mGoogleApiClient.connect();
        } else {
            requestLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this.context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    @Override
    @SuppressLint({"MissingPermission"})
    public void onConnected(Bundle bundle) {
        Log.i("位置", LocationServices.FusedLocationApi.getLocationAvailability(this.mGoogleApiClient) + "");
        requestLocationUpdates();
    }

    @SuppressLint({"MissingPermission"})
    public void requestLocationUpdates() {
        if (this.mGoogleApiClient.isConnected()) {
            locationRequest();
        }
    }

    @SuppressLint({"MissingPermission"})
    public void locationRequest() {
        LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.locationRequest, this, null);
    }

    @Override
    public void onLocationResult(LocationResult result) {
        TimeStampState.getInstance().setTimeStamp(result.getLastLocation().getTime());
        if (this.locationMarker == null) {
            if (result.getLastLocation() != null) {
                LatLng latLng = new LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude());
                this.locationMarker = this.mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map)).anchor(0.5f, 0.5f));
                this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                return;
            }
            return;
        }
        onLocationChanged(result.getLastLocation());
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        Log.i("位置", "onLocationAvailability: isLocationAvailable =  " + locationAvailability.isLocationAvailable());
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void onLocationChanged(Location location) {
        startMoveLocationAndMap(location);
        float verticalAccuracyMeter = 0.0f;
        if (Build.VERSION.SDK_INT >= 26) {
            verticalAccuracyMeter = location.getVerticalAccuracyMeters();
        }
        X8PressureGpsInfo.getInstance().setmLongitude(location.getLongitude());
        X8PressureGpsInfo.getInstance().setmLatitude(location.getLatitude());
        X8PressureGpsInfo.getInstance().setmAltitude(location.getAltitude());
        X8PressureGpsInfo.getInstance().setmHorizontalAccuracyMeters(location.getAccuracy());
        X8PressureGpsInfo.getInstance().setmVerticalAccuracyMeters(verticalAccuracyMeter);
        X8PressureGpsInfo.getInstance().setmSpeed(location.getSpeed());
        X8PressureGpsInfo.getInstance().setmBearing(location.getBearing());
        X8PressureGpsInfo.getInstance().setHasLocation(true);
        this.accuracy = location.getAccuracy();
        getCityThread(location);
    }

    private void startMoveLocationAndMap(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.i("位置", "latLng" + latLng);
        if (this.locationMarker != null) {
            this.locationMarker.setPosition(latLng);
        }
    }

    public void onSensorChanged(float degree) {
        if (this.locationMarker != null) {
            float f = this.mGoogleMap.getCameraPosition().bearing;
            this.locationMarker.setRotation(90.0f + degree);
        }
    }

    public void addHomeLocation(LatLng latLng) {
        if (this.home == null) {
            this.home = this.mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_point)).anchor(0.5f, 1.0f));
        } else {
            this.home.setPosition(latLng);
        }
    }

    public void addDeviceLocation(LatLng latLng) {
        if (this.deviceMarker == null) {
            this.deviceMarker = this.mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_fly_handpiece_location)).anchor(0.5f, 0.5f));
        } else {
            this.deviceMarker.setPosition(latLng);
        }
    }

    public void moveCameraByDevice() {
        LatLng latLng = getDevLocation();
        if (latLng != null) {
            float zoom = this.mGoogleMap.getCameraPosition().zoom;
            VisibleRegion visibleRegion = this.mGoogleMap.getProjection().getVisibleRegion();
            LatLng farLeft = visibleRegion.farLeft;
            LatLng nearRight = visibleRegion.nearRight;
            Point point = this.mGoogleMap.getProjection().toScreenLocation(latLng);
            Point topLeft = this.mGoogleMap.getProjection().toScreenLocation(farLeft);
            Point bottomRight = this.mGoogleMap.getProjection().toScreenLocation(nearRight);
            boolean b = topLeft.x <= point.x && point.x <= bottomRight.x && topLeft.y <= point.y && point.y <= bottomRight.y;
            if (!b) {
                this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            }
        }
    }

    public void clearMarker() {
        if (this.deviceMarker != null) {
            this.deviceMarker.remove();
            this.deviceMarker = null;
        }
        if (this.home != null) {
            this.home.remove();
            this.home = null;
        }
        clearFlyPolyLine();
    }

    public void drawFlyLine() {
    }

    public void chaneDeviceAngle(float angle) {
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        this.deviceMarker.setRotation(angle - this.mGoogleMap.getCameraPosition().bearing);
    }

    public void clearFlyPolyLine() {
        if (this.flyPolyLine != null) {
            this.flyPolyLine.remove();
            this.flyPolyLine = null;
        }
    }

    public void addFlyPolyLine(double latitude, double logitude) {
    }

    public LatLng getHomeLocation() {
        if (this.home != null) {
            return this.home.getPosition();
        }
        return null;
    }

    public LatLng getManLocation() {
        if (this.locationMarker != null) {
            return this.locationMarker.getPosition();
        }
        return null;
    }

    public LatLng getDevLocation() {
        if (this.deviceMarker != null) {
            return this.deviceMarker.getPosition();
        }
        return null;
    }

    public void animatePersonLocation() {
        if (this.locationMarker != null) {
            LatLng latLng = this.locationMarker.getPosition();
            this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
        }
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public void getCity(Location location) {
        Geocoder ge = new Geocoder(this.context);
        List<Address> addList = null;
        try {
            addList = ge.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address ad = addList.get(i);
                X8MapGetCityManager.locality = ad.getLocality();
            }
        }
        this.state = 0;
    }

    public void getCityThread(final Location location) {
        if (X8MapGetCityManager.locality == null) {
            X8MapGetCityManager.locality = "";
        }
        if (X8MapGetCityManager.locality.equals("") && this.state == 0) {
            this.state = 1;
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    GglMapLocationManager.this.getCity(location);
                }
            });
        }
    }

    public double[] getDevicePosition() {
        if (this.deviceMarker != null) {
            LatLng latLng = this.deviceMarker.getPosition();
            return new double[]{latLng.latitude, latLng.longitude};
        }
        return null;
    }
}

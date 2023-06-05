package com.fimi.app.x8s.map.manager.google;

import android.content.Context;
import android.graphics.Point;

import com.fimi.android.app.R;
import com.fimi.app.x8s.map.interfaces.AbsAiSurroundManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.google.GglMapCustomMarkerView;
import com.fimi.app.x8s.tools.GeoTools;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.util.GpsCorrect;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GglMapAiSurroundManager extends AbsAiSurroundManager {
    private static final PatternItem DASH = new Dash(20.0f);
    private static final PatternItem GAP = new Gap(20.0f);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DASH);
    List<LatLng> latLngs = new ArrayList();
    MapPointLatLng mp;
    private final Context context;
    private final GglMapLocationManager gglMapLocationManager;
    private final GoogleMap googleMap;
    private Circle limitCircle;
    private Polyline line;
    private Polygon mPolygon;
    private Marker pointMarker;
    private Polyline polyline;
    private Circle surroundCircle;

    public GglMapAiSurroundManager(Context context, GoogleMap googleMap, GglMapLocationManager gglMapLocationManager) {
        this.context = context;
        this.googleMap = googleMap;
        this.gglMapLocationManager = gglMapLocationManager;
    }

    @Override
    public void clearSurroundMarker() {
        if (this.pointMarker != null) {
            this.pointMarker.remove();
            this.pointMarker = null;
        }
        if (this.polyline != null) {
            this.polyline.remove();
            this.polyline = null;
        }
        if (this.mPolygon != null) {
            this.mPolygon.remove();
            this.mPolygon = null;
        }
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        if (this.surroundCircle != null) {
            this.surroundCircle.remove();
            this.surroundCircle = null;
        }
        if (this.latLngs != null) {
            this.latLngs.clear();
        }
        if (this.line != null) {
            this.line.remove();
            this.line = null;
        }
        this.mp = null;
    }

    @Override
    public void setAiSurroundMark(double latitude, double logitude) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        LatLng latLng = new LatLng(fLatLng.latitude, fLatLng.longitude);
        if (this.pointMarker == null) {
            GglMapCustomMarkerView gdCustemMarkerView = new GglMapCustomMarkerView();
            BitmapDescriptor mBitmapDescriptor = gdCustemMarkerView.createCustomMarkerView(this.context, R.drawable.x8_img_ai_follow_point2);
            this.mp = new MapPointLatLng();
            this.pointMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(true));
            this.pointMarker.setTag(this.mp);
            return;
        }
        this.pointMarker.setPosition(latLng);
    }

    @Override
    public float getSurroundRadius(double lastLogitude, double lastLatitude, double currentLogitude, double currentLatitude) {
        FLatLng last = GpsCorrect.Earth_To_Mars(lastLatitude, lastLogitude);
        FLatLng currrent = GpsCorrect.Earth_To_Mars(currentLatitude, currentLogitude);
        float distance = (float) GeoTools.getDistance(new LatLng(last.latitude, last.longitude), new LatLng(currrent.latitude, currrent.longitude)).valueInMeters();
        return distance;
    }

    @Override
    public void setAiSurroundCircle(double latitude, double logitude, float radius) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        LatLng lats = new LatLng(fLatLng.latitude, fLatLng.longitude);
        drawAiSurroundCircle(lats.latitude, lats.longitude, radius);
    }

    @Override
    public float getLineAngleByMapBealing(float angle) {
        return 0.0f;
    }

    @Override
    public void setOnMapClickListener() {
    }

    @Override
    public void removeMapClickListener() {
    }

    @Override
    public void resetMapEvent() {
    }

    @Override
    public void drawAiLimit(double lat, double lng, double radiu) {
        if (this.limitCircle == null) {
            CircleOptions circle = new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokePattern(PATTERN_POLYLINE_DOTTED).strokeColor(this.context.getResources().getColor(R.color.x8_drone_inface_line)).fillColor(this.fillColor).strokeWidth(this.strokeWidth);
            this.limitCircle = this.googleMap.addCircle(circle);
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
    }

    @Override
    public void addEllipse(double lat, double lng, float horizontalDistance, float startNosePoint) {
        if (this.mPolygon != null) {
            this.mPolygon.remove();
            this.mPolygon = null;
        }
        float radius = (horizontalDistance / 2.0f) + 2.0f;
        FLatLng centerpoint = GpsCorrect.Earth_To_Mars(lat, lng);
        double phase = 6.283185307179586d / 360;
        PolygonOptions options = new PolygonOptions();
        options.strokePattern(PATTERN_POLYLINE_DOTTED);
        VisibleRegion visibleRegion = this.googleMap.getProjection().getVisibleRegion();
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        Point pLeft = this.googleMap.getProjection().toScreenLocation(farLeft);
        Point pRight = this.googleMap.getProjection().toScreenLocation(nearRight);
        int pix = pRight.x - pLeft.x;
        double scale = GeoTools.getScale(farLeft, nearRight, pix);
        double t = radius * scale;
        double a = radius;
        double c = a * 0.7d;
        double b = Math.sqrt((a * a) - (c * c));
        for (int i = 0; i < 360; i++) {
            Point p1 = this.googleMap.getProjection().toScreenLocation(new LatLng(centerpoint.latitude, centerpoint.longitude));
            double x1 = p1.x + (Math.cos(i * phase) * t);
            double y1 = p1.y + ((b / a) * t * Math.sin(i * phase));
            double newX = p1.x + (((x1 - p1.x) * Math.cos(startNosePoint * phase)) - ((y1 - p1.y) * Math.sin(startNosePoint * phase)));
            double newY = p1.y + ((x1 - p1.x) * Math.sin(startNosePoint * phase)) + ((y1 - p1.y) * Math.cos(startNosePoint * phase));
            double x2 = newX - (Math.cos(startNosePoint * phase) * t);
            double y2 = newY - (Math.sin(startNosePoint * phase) * t);
            LatLng p2 = this.googleMap.getProjection().fromScreenLocation(new Point((int) x2, (int) y2));
            options.add(p2);
        }
        this.mPolygon = this.googleMap.addPolygon(options.strokeWidth(3.0f));
        this.mPolygon.setStrokeColor(this.context.getResources().getColor(R.color.x8_drone_inface_line));
    }

    @Override
    public void drawAiSurroundCircle(double lat, double lnt, double radiu) {
        if (this.surroundCircle == null) {
            CircleOptions circle = new CircleOptions().center(new LatLng(lat, lnt)).radius(radiu).strokeColor(this.context.getResources().getColor(R.color.x8_drone_inface_line)).fillColor(this.fillColor).strokeWidth(this.strokeWidth);
            this.surroundCircle = this.googleMap.addCircle(circle);
            return;
        }
        this.surroundCircle.setCenter(new LatLng(lat, lnt));
    }

    @Override
    public void addPolylinescircle(boolean cw, double lat, double lng, double lat1, double lng2, int radius, int maxRadius) {
        float startAngle;
        if (this.line != null) {
            this.line.remove();
            this.line = null;
        }
        float angle = this.mapCalcAngle.getAngle2(new LatLng(lat, lng), new LatLng(lat1, lng2));
        PolylineOptions options = new PolylineOptions();
        options.pattern(PATTERN_POLYLINE_DOTTED);
        double phase = 6.283185307179586d / 360;
        int time = (int) Math.round(((radius * 2) * 3.141592653589793d) / 10.0d);
        if (time < 50) {
            time = 50;
        } else if (time > 180) {
            time = 180;
        }
        double temp = (((maxRadius + 10) - radius) * 1.0f) / time;
        VisibleRegion visibleRegion = this.googleMap.getProjection().getVisibleRegion();
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        Point pLeft = this.googleMap.getProjection().toScreenLocation(farLeft);
        Point pRight = this.googleMap.getProjection().toScreenLocation(nearRight);
        int pix = pRight.x - pLeft.x;
        float scale = GeoTools.getScale(farLeft, nearRight, pix);
        for (int i = 0; i < 360; i++) {
            double d = radius + ((i / (360.0f / time)) * temp);
            float t = (float) (scale * d);
            if (!cw) {
                float cc = angle - i;
                if (cc < 0.0f) {
                    startAngle = 360.0f + cc;
                } else {
                    startAngle = cc;
                }
            } else {
                startAngle = i + angle;
            }
            Point p1 = this.googleMap.getProjection().toScreenLocation(new LatLng(lat, lng));
            int x1 = (int) (p1.x + (t * Math.cos(startAngle * phase)));
            int y1 = (int) (p1.y + (t * Math.sin(startAngle * phase)));
            LatLng p2 = this.googleMap.getProjection().fromScreenLocation(new Point(x1, y1));
            options.add(p2);
        }
        this.line = this.googleMap.addPolyline(options.width(3.0f));
        this.line.setColor(this.context.getResources().getColor(R.color.x8_drone_inface_line));
    }

    @Override
    public void reSetAiSurroundCircle(double latitude, double logitude, float radius) {
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        setAiSurroundCircle(latitude, logitude, radius);
    }
}

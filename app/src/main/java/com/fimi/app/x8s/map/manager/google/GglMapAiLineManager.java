package com.fimi.app.x8s.map.manager.google;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.map.interfaces.AbsAiLineManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.map.view.google.GglMapCustomMarkerView;
import com.fimi.app.x8s.tools.GeoTools;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.dataparser.AckGetAiLinePoint;
import com.fimi.x8sdk.dataparser.AckGetAiLinePointsAction;
import com.fimi.x8sdk.entity.FLatLng;
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
import java.util.Iterator;
import java.util.List;


public class GglMapAiLineManager extends AbsAiLineManager implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {
    private final int MAX = 20;
    private final Context context;
    private final GglMapLocationManager gglMapLocationManager;
    private final GoogleMap googleMap;
    private final List<MapPointLatLng> mMapPointList = new ArrayList();
    private final List<Marker> mMarkerList = new ArrayList();
    private final List<Marker> interestMarkerList = new ArrayList();
    private final List<Marker> arrowMarkerList = new ArrayList();
    private final GglMapCustomMarkerView gdCustemMarkerView = new GglMapCustomMarkerView();
    private Circle limitCircle;
    private IX8MarkerListener lineMarkerSelectListener;
    private Marker mSelectMarker = null;
    private boolean isClick = true;
    private int nPos = -1;

    public GglMapAiLineManager(Context context, GoogleMap googleMap, GglMapLocationManager gglMapLocationManager) {
        this.context = context;
        this.googleMap = googleMap;
        this.gglMapLocationManager = gglMapLocationManager;
    }

    @Override
    public void setMarkerViewInfo(float height) {
        if (this.mSelectMarker != null) {
            MapPointLatLng mp = (MapPointLatLng) this.mSelectMarker.getTag();
            if (mp.isSelect) {
                mp.altitude = height;
                changePointMarker(this.mSelectMarker, mp, false);
            }
        }
    }

    @Override
    public float getLineAngleByMapBealing(float angle) {
        return angle - this.googleMap.getCameraPosition().bearing;
    }

    @Override
    public void setOnMapClickListener() {
        setOnMarkerListener();
    }

    @Override
    public void removeMapClickListener() {
        this.googleMap.setOnMapClickListener(null);
        this.googleMap.setOnMarkerClickListener(null);
    }

    @Override
    public void resetMapEvent() {
        this.googleMap.setOnMapClickListener(null);
        removeMarkerListener();
        this.googleMap.setOnMarkerDragListener(null);
    }

    @Override
    public void drawAiLimit(double lat, double lng, double radiu) {
        if (this.limitCircle == null) {
            CircleOptions circle = new CircleOptions().center(new LatLng(lat, lng)).radius(radiu).strokeColor(this.lineLimitColor).fillColor(this.fillColor).strokeWidth(this.strokeWidth);
            this.limitCircle = this.googleMap.addCircle(circle);
            return;
        }
        this.limitCircle.setCenter(new LatLng(lat, lng));
    }

    public void removeMarkerListener() {
        this.googleMap.setOnMarkerClickListener(null);
    }

    public boolean isFullSize() {
        return this.mMapPointList.size() == 20;
    }

    public boolean isValid(LatLng latLng) {
        boolean ret = true;
        if (this.mMapPointList.size() < 1) {
            return true;
        }
        Iterator<MapPointLatLng> it = this.mMapPointList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MapPointLatLng mapPointLatLng = it.next();
            float distance = (float) GeoTools.getDistance(latLng, new LatLng(mapPointLatLng.latitude, mapPointLatLng.longitude)).valueInMeters();
            if (distance <= 10.0f) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    @Override
    public void setAiLineMark(double latitude, double logitude, float height, float angle) {
        FLatLng fLatLng = GpsCorrect.Earth_To_Mars(latitude, logitude);
        LatLng latLng = new LatLng(fLatLng.latitude, fLatLng.longitude);
        this.nPos = -1;
        onAiLineAddPoint(latLng, height, angle);
    }

    @Override
    public void deleteMarker(boolean isMapPoint) {
        deleteMarker(isMapPoint, this.gglMapLocationManager.getDevLocation());
    }

    @Override
    public int getAiLinePointSize() {
        return this.mMapPointList.size();
    }

    @Override
    public float getAiLineDistance() {
        float distance = 0.0f;
        if (this.mMarkerList.size() == 2) {
            float distance2 = (float) GeoTools.getDistance(this.mMarkerList.get(0).getPosition(), this.mMarkerList.get(1).getPosition()).valueInMeters();
            return distance2;
        }
        for (int i = 0; i < this.mMarkerList.size(); i++) {
            if (i != 0) {
                distance += (float) GeoTools.getDistance(this.mMarkerList.get(i - 1).getPosition(), this.mMarkerList.get(i).getPosition()).valueInMeters();
            }
        }
        return distance;
    }

    @Override
    public List<MapPointLatLng> getMapPointList() {
        return this.mMapPointList;
    }

    @Override
    public List<MapPointLatLng> getMapPointList(MapPointLatLng mpl) {
        List<MapPointLatLng> list = new ArrayList<>();
        for (MapPointLatLng p : this.mMapPointList) {
            if (p.mInrertestPoint == null) {
                list.add(p);
            } else if (p.mInrertestPoint == mpl) {
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public boolean isInterestBeBind(MapPointLatLng mpl) {
        new ArrayList();
        for (MapPointLatLng p : this.mMapPointList) {
            if (p.mInrertestPoint == mpl) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeInterestUnBebind() {
        Marker tmp = null;
        Iterator<Marker> it = this.interestMarkerList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Marker m = it.next();
            MapPointLatLng mpl = (MapPointLatLng) m.getTag();
            boolean isFind = false;
            Iterator<MapPointLatLng> it2 = this.mMapPointList.iterator();
            while (true) {
                if (it2.hasNext()) {
                    MapPointLatLng p = it2.next();
                    if (p.mInrertestPoint != null && p.mInrertestPoint == mpl) {
                        isFind = true;
                        break;
                    }
                }
            }
            if (!isFind) {
                tmp = m;
                break;
            }
        }
        if (tmp != null) {
            this.interestMarkerList.remove(tmp);
            tmp.remove();
            int i = 0;
            for (Marker marker : this.interestMarkerList) {
                i++;
                MapPointLatLng p2 = (MapPointLatLng) marker.getTag();
                p2.nPos = i;
                changePointMarker(marker, p2, false);
            }
        }
        if (this.lineMarkerSelectListener != null) {
            this.lineMarkerSelectListener.onInterestSizeEnable(this.interestMarkerList.size() < 20);
        }
    }

    @Override
    public boolean hasPointUnBind() {
        new ArrayList();
        for (MapPointLatLng p : this.mMapPointList) {
            if (p.mInrertestPoint == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateInterestBindPoint(MapPointLatLng mpl, int index) {
        boolean isRelation;
        int i = 0;
        while (true) {
            if (i >= this.mMapPointList.size()) {
                break;
            } else if (i != index) {
                i++;
            } else {
                MapPointLatLng tmp = this.mMapPointList.get(index);
                tmp.mInrertestPoint = mpl;
                if (mpl != null) {
                    tmp.setAngle(getPointAngle(tmp, mpl));
                    isRelation = true;
                } else {
                    isRelation = false;
                }
                changePointMarker(this.mMarkerList.get(i), tmp, isRelation);
            }
        }
        if (this.lineMarkerSelectListener != null) {
            this.lineMarkerSelectListener.onInterestSizeEnable(this.interestMarkerList.size() < 20);
        }
    }

    @Override
    public List<MapPointLatLng> getInterestPointList() {
        List<MapPointLatLng> list = new ArrayList<>();
        for (int i = 0; i < this.interestMarkerList.size(); i++) {
            list.add((MapPointLatLng) this.interestMarkerList.get(i).getTag());
        }
        return list;
    }

    @Override
    public void setLineMarkerSelectListener(IX8MarkerListener listener) {
        this.lineMarkerSelectListener = listener;
    }

    @Override
    public void clearAiLineMarker() {
        clearPointMark();
    }

    @Override
    public void startAiLineProcess() {
        if (this.mSelectMarker != null) {
            onMarkerClick(this.mSelectMarker);
        }
        this.nPos = -1;
    }

    @Override
    public void setAiLineIndexPoint(int index) {
        int index2;
        if (this.nPos != index) {
            if (this.nPos == -1 && index >= 0) {
                for (int j = 0; j < index; j++) {
                    if (this.mMarkerList.size() > j) {
                        Marker lastMarker = this.mMarkerList.get(j);
                        MapPointLatLng lastMarkerPoint = (MapPointLatLng) lastMarker.getTag();
                        lastMarkerPoint.isSelect = false;
                        changeViewBySetPoints(lastMarker, lastMarkerPoint, false, true);
                        lightInterestPointByRunning(lastMarkerPoint, false);
                    } else {
                        return;
                    }
                }
                for (int j2 = 1; j2 < index; j2++) {
                    if (this.mMarkerList.size() > j2) {
                        Marker lastMarker2 = this.mMarkerList.get(j2);
                        MapPointLatLng lastMarkerPoint2 = (MapPointLatLng) lastMarker2.getTag();
                        if (lastMarkerPoint2.yawMode == 0) {
                            if (this.arrowMarkerList.size() > 0 && lastMarkerPoint2.mInrertestPoint != null) {
                                int nIindex = findIndexByInterest(lastMarkerPoint2);
                                int n = nIindex * 2;
                                if (this.arrowMarkerList.size() > n + 1) {
                                    changeLineSmallMarkerByRunning(this.arrowMarkerList.get(n), this.arrowMarkerList.get(n + 1), lastMarkerPoint2.showAngle, 2);
                                }
                            }
                        } else {
                            int n2 = j2 * 2;
                            if (this.arrowMarkerList.size() > n2 + 1) {
                                changeLineSmallMarkerByRunning(this.arrowMarkerList.get(n2), this.arrowMarkerList.get(n2 + 1), lastMarkerPoint2.showAngle, 2);
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
            if (this.nPos >= 0) {
                Marker lastMarker3 = this.mMarkerList.get(this.nPos);
                MapPointLatLng lastMarkerPoint3 = (MapPointLatLng) lastMarker3.getTag();
                lastMarkerPoint3.isSelect = false;
                changeViewBySetPoints(lastMarker3, lastMarkerPoint3, false, true);
                lightInterestPointByRunning(lastMarkerPoint3, false);
            }
            Marker currentMarker = this.mMarkerList.get(index);
            MapPointLatLng mapPointLatLng = (MapPointLatLng) currentMarker.getTag();
            mapPointLatLng.isSelect = true;
            changeViewByRunning(currentMarker, mapPointLatLng);
            lightInterestPointByRunning(mapPointLatLng, true);
            this.nPos = index;
            if (this.lineMarkerSelectListener != null) {
                this.lineMarkerSelectListener.onRunIndex(index + 1, mapPointLatLng.action);
            }
            for (int i = 0; i < this.polylineList.size(); i++) {
                if (i == this.nPos) {
                    this.polylineList.get(i).remove();
                    Polyline c = this.polylineList.get(i);
                    this.polylineList.set(i, getPolyline(this.nPos, c, this.lineRunningColor));
                } else if (i < this.nPos) {
                    this.polylineList.get(i).remove();
                    Polyline c2 = this.polylineList.get(i);
                    this.polylineList.set(i, getPolyline(this.nPos, c2, this.lineRunColor));
                } else {
                    this.polylineList.get(i).remove();
                    Polyline c3 = this.polylineList.get(i);
                    this.polylineList.set(i, getPolyline(this.nPos, c3, this.lineDefaultColor));
                }
            }
            if (mapPointLatLng.yawMode == 0) {
                if (this.arrowMarkerList.size() > 0 && this.nPos > 0) {
                    if (mapPointLatLng.mInrertestPoint != null && (index2 = findIndexByInterest(mapPointLatLng)) >= 0) {
                        int n3 = index2 * 2;
                        if (n3 + 1 < this.arrowMarkerList.size()) {
                            changeLineSmallMarkerByRunning(this.arrowMarkerList.get(n3), this.arrowMarkerList.get(n3 + 1), mapPointLatLng.showAngle, 1);
                        }
                    }
                    if (this.nPos - 1 > 0) {
                        Marker lastMarker4 = this.mMarkerList.get(this.nPos - 1);
                        MapPointLatLng lastMarkerPoint4 = (MapPointLatLng) lastMarker4.getTag();
                        if (lastMarkerPoint4.mInrertestPoint != null) {
                            int index3 = findIndexByInterest(lastMarkerPoint4);
                            int n4 = index3 * 2;
                            if (index3 >= 0 && n4 + 1 < this.arrowMarkerList.size()) {
                                changeLineSmallMarkerByRunning(this.arrowMarkerList.get(n4), this.arrowMarkerList.get(n4 + 1), lastMarkerPoint4.showAngle, 2);
                            }
                        }
                    }
                }
            } else if (this.arrowMarkerList.size() > 0 && this.nPos > 0) {
                int n5 = (this.nPos - 1) * 2;
                if (n5 + 1 < this.arrowMarkerList.size()) {
                    changeLineSmallMarkerByRunning(this.arrowMarkerList.get(n5), this.arrowMarkerList.get(n5 + 1), mapPointLatLng.showAngle, 1);
                    if (n5 > 0) {
                        Marker lastMarker5 = this.mMarkerList.get(this.nPos - 1);
                        MapPointLatLng lastMarkerPoint5 = (MapPointLatLng) lastMarker5.getTag();
                        int n6 = (this.nPos - 2) * 2;
                        if (this.arrowMarkerList.size() > n6 + 1) {
                            changeLineSmallMarkerByRunning(this.arrowMarkerList.get(n6), this.arrowMarkerList.get(n6 + 1), lastMarkerPoint5.showAngle, 2);
                        }
                    }
                }
            }
        }
    }

    public int findIndexByInterest(MapPointLatLng mpl) {
        int index = -1;
        for (int i = 1; i < this.mMarkerList.size(); i++) {
            MapPointLatLng current = (MapPointLatLng) this.mMarkerList.get(i).getTag();
            if (current.mInrertestPoint != null) {
                index++;
                if (mpl == current) {
                    break;
                }
            }
        }
        return index;
    }

    public Polyline getPolyline(int index, Polyline pl, int color) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(pl.getPoints());
        polylineOptions.color(this.context.getResources().getColor(color)).zIndex(3.0f);
        polylineOptions.width(4.0f);
        Polyline line = this.googleMap.addPolyline(polylineOptions);
        line.setPattern(PATTERN_DASHED);
        return line;
    }

    @Override
    public void setAiLineMarkActionByDevice(List<AckGetAiLinePointsAction> points) {
        if (this.mMarkerList != null && this.mMarkerList.size() > 0) {
            for (Marker m : this.mMarkerList) {
                MapPointLatLng mpl = (MapPointLatLng) m.getTag();
                Iterator<AckGetAiLinePointsAction> it = points.iterator();
                while (true) {
                    if (it.hasNext()) {
                        AckGetAiLinePointsAction pointsAction = it.next();
                        if (mpl.nPos - 1 == pointsAction.pos) {
                            mpl.action = pointsAction.getAction();
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setAiLineMarkByHistory(List<X8AiLinePointLatlngInfo> points, int mapTpye) {
        int res;
        for (X8AiLinePointLatlngInfo point : points) {
            MapPointLatLng mp = new MapPointLatLng();
            if (point.getYawMode() == 0) {
                res = R.drawable.x8_ai_line_point_no_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createCustomMarkerView2(this.context, res, this.mMarkerList.size() + 1);
            mp.altitude = point.getAltitude();
            mp.nPos = this.mMarkerList.size() + 1;
            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
            mp.longitude = latLng.longitude;
            mp.latitude = latLng.latitude;
            mMarker.setFlat(true);
            mMarker.setTag(mp);
            this.mMarkerList.add(mMarker);
            mp.distance = 0.0f;
            this.mMapPointList.add(mp);
        }
        if (points.size() > 0) {
            drawPointLine(this.gglMapLocationManager.getDevLocation());
        }
    }

    @Override
    public void updataAngle(int i, float angle) {
        Marker marker = this.mMarkerList.get(i);
        MapPointLatLng p = (MapPointLatLng) marker.getTag();
        p.showAngle = angle;
    }

    @Override
    public MapPointLatLng getMapPointLatLng() {
        if (this.mSelectMarker == null) {
            return null;
        }
        return (MapPointLatLng) this.mSelectMarker.getTag();
    }

    @Override
    public void addInreterstMarker(int x, int y) {
        LatLng latLng = this.googleMap.getProjection().fromScreenLocation(new Point(x, y));
        MapPointLatLng mp = new MapPointLatLng();
        mp.isIntertestPoint = true;
        mp.isMapPoint = true;
        mp.nPos = this.interestMarkerList.size() + 1;
        float height = StateManager.getInstance().getX8Drone().getHeight();
        int h = Math.round(height);
        if (h <= 5) {
            h = 5;
        }
        mp.altitude = h;
        mp.latitude = latLng.latitude;
        mp.longitude = latLng.longitude;
        Marker interestMarker = addInterestMarker(latLng, h, mp);
        interestMarker.setTag(mp);
        interestMarker.setDraggable(false);
        this.interestMarkerList.add(interestMarker);
        if (this.lineMarkerSelectListener != null) {
            this.lineMarkerSelectListener.onInterestSizeEnable(this.interestMarkerList.size() < 20);
        }
        this.isClick = false;
        onMarkerClick(interestMarker);
    }

    public void addInterestByDevice(AckGetAiLinePoint point) {
        MapPointLatLng mp = new MapPointLatLng();
        mp.isIntertestPoint = true;
        mp.isMapPoint = true;
        mp.nPos = this.interestMarkerList.size() + 1;
        mp.altitude = (float) point.getAltitudePOI();
        mp.latitude = point.getLatitudePOI();
        mp.longitude = point.getLongitudePOI();
        Marker interestMarker = addInterestMarker(new LatLng(mp.latitude, mp.longitude), mp.altitude, mp);
        interestMarker.setTag(mp);
        interestMarker.setDraggable(true);
        this.interestMarkerList.add(interestMarker);
    }

    public int findInterestPoint(AckGetAiLinePoint point, List<AckGetAiLinePoint> interestPoints) {
        int i = -1;
        boolean isFind = false;
        Iterator<AckGetAiLinePoint> it = interestPoints.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AckGetAiLinePoint o = it.next();
            i++;
            if (point.getLongitudePOI() == o.getLongitudePOI() && point.getLatitudePOI() == o.getLatitudePOI() && point.getAltitudePOI() == o.getAltitudePOI()) {
                isFind = true;
                break;
            }
        }
        if (!isFind) {
            return -1;
        }
        return i;
    }

    @Override
    public MapPointLatLng getInterstMakerLatLng() {
        return null;
    }

    @Override
    public void removeInterstPointByRunning() {
        for (Marker m : this.interestMarkerList) {
            m.setDraggable(false);
        }
    }

    @Override
    public float getPointAngle(MapPointLatLng from, MapPointLatLng to) {
        float angle = this.mapCalcAngle.getAngle(new LatLng(from.latitude, from.longitude), new LatLng(to.latitude, to.longitude)) % 360.0f;
        if (angle < 0.0f) {
            return angle + 360.0f;
        }
        return angle;
    }

    @Override
    public void notityChangeView(MapPointLatLng des, boolean isRelation) {
        lightPoint(des, isRelation);
    }

    @Override
    public void notityChangeView(MapPointLatLng des) {
        for (Marker marker : this.mMarkerList) {
            MapPointLatLng p = (MapPointLatLng) marker.getTag();
            if (des == p) {
                changePointMarker(marker, des, false);
                lightPoint(des, true);
                return;
            }
        }
    }

    private void setOnMarkerListener() {
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMarkerDragListener(this);
    }

    private void clearPointMark() {
        for (Marker marker : this.mMarkerList) {
            marker.remove();
        }
        for (Marker marker2 : this.interestMarkerList) {
            marker2.remove();
        }
        for (Marker marker3 : this.arrowMarkerList) {
            marker3.remove();
        }
        clearMarker();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        onMapClickForAiLine(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        MapPointLatLng mp;
        if (isOnMarkerClickValid() && (mp = (MapPointLatLng) marker.getTag()) != null) {
            if (this.mSelectMarker != null) {
                if (this.mSelectMarker.getTag() == marker.getTag()) {
                    mp.isSelect = false;
                    this.mSelectMarker = null;
                    changePointMarker(marker, mp, false);
                    lightPoint(mp, false);
                } else {
                    Marker lastMarker = this.mSelectMarker;
                    MapPointLatLng lastMp = (MapPointLatLng) lastMarker.getTag();
                    lastMp.isSelect = false;
                    changePointMarker(lastMarker, lastMp, false);
                    lightPoint(lastMp, false);
                    this.mSelectMarker = marker;
                    mp.isSelect = true;
                    changePointMarker(this.mSelectMarker, mp, false);
                    lightPoint(mp, true);
                }
            } else {
                this.mSelectMarker = marker;
                mp.isSelect = true;
                changePointMarker(this.mSelectMarker, mp, false);
                lightPoint(mp, true);
            }
            this.lineMarkerSelectListener.onMarkerSelect(mp.isSelect, mp.altitude, mp, this.isClick);
            this.isClick = true;
        }
        return true;
    }

    public void lightPoint(MapPointLatLng mpl, boolean isRelation) {
        if (mpl.isMapPoint) {
            if (mpl.isIntertestPoint) {
                for (Marker marker : this.mMarkerList) {
                    MapPointLatLng tmp = (MapPointLatLng) marker.getTag();
                    if (tmp.mInrertestPoint == mpl) {
                        changePointMarker(marker, tmp, isRelation);
                    }
                }
            } else if (mpl.mInrertestPoint != null) {
                for (Marker interest : this.interestMarkerList) {
                    MapPointLatLng tmp2 = (MapPointLatLng) interest.getTag();
                    if (mpl.mInrertestPoint == tmp2) {
                        changePointMarker(interest, tmp2, isRelation);
                        return;
                    }
                }
            }
        }
    }

    public void lightInterestPointByRunning(MapPointLatLng mpl, boolean isLight) {
        if (mpl.isMapPoint && mpl.mInrertestPoint != null) {
            for (Marker interest : this.interestMarkerList) {
                MapPointLatLng tmp = (MapPointLatLng) interest.getTag();
                if (mpl.mInrertestPoint == tmp) {
                    tmp.isSelect = isLight;
                    changePointMarker(interest, tmp, false);
                    return;
                }
            }
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        int res = R.drawable.x8_img_ai_line_inreterst_max2;
        MapPointLatLng mp = (MapPointLatLng) marker.getTag();
        BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createMapPioView(this.context, res, mp.altitude, mp.nPos, true, false);
        marker.setIcon(mBitmapDescriptor);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if (this.lineMarkerSelectListener != null) {
            Rect rect = this.lineMarkerSelectListener.getDeletePosition();
            LatLng mLatlng = marker.getPosition();
            Point mPoint = this.googleMap.getProjection().toScreenLocation(mLatlng);
            if (rect.left > mPoint.x || mPoint.x > rect.right || rect.top > mPoint.y || mPoint.y <= rect.bottom) {
            }
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        int res = R.drawable.x8_img_ai_line_inreterst_max1;
        MapPointLatLng mp = (MapPointLatLng) marker.getTag();
        BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createMapPioView(this.context, res, mp.altitude, mp.nPos, false, false);
        marker.setIcon(mBitmapDescriptor);
        MapPointLatLng tmp = (MapPointLatLng) marker.getTag();
        tmp.isInrertestPointActive = true;
        tmp.latitude = marker.getPosition().latitude;
        tmp.longitude = marker.getPosition().longitude;
        int i = 0;
        for (MapPointLatLng mpl : this.mMapPointList) {
            if (mpl.mInrertestPoint != null && tmp == mpl.mInrertestPoint) {
                mpl.setAngle(getPointAngle(mpl, tmp));
                changePointMarker(this.mMarkerList.get(i), mpl, false);
            }
            i++;
        }
    }

    public void addPointLatLng(LatLng latLng, float distance, LatLng deviceLocation, boolean isMapPoint, float angle) {
        float height;
        MapPointLatLng mp = new MapPointLatLng();
        mp.nPos = this.mMarkerList.size() + 1;
        if (isMapPoint) {
            if (this.mMarkerList.size() == 0) {
                height = StateManager.getInstance().getX8Drone().getHeight();
                if (height < 5.0f) {
                    height = 5.0f;
                }
            } else {
                MapPointLatLng lastMpl = (MapPointLatLng) this.mMarkerList.get(this.mMarkerList.size() - 1).getTag();
                height = lastMpl.altitude;
            }
        } else {
            height = StateManager.getInstance().getX8Drone().getHeight();
            if (height < 5.0f) {
                height = 5.0f;
            }
        }
        mp.yawMode = this.lineMarkerSelectListener.getOration();
        int h = Math.round(height);
        Marker mMarker = addPointMarker(isMapPoint, latLng, mp, h, angle);
        mp.longitude = latLng.longitude;
        mp.latitude = latLng.latitude;
        mMarker.setTag(mp);
        this.mMarkerList.add(mMarker);
        mp.distance = distance;
        this.mMapPointList.add(mp);
        drawPointLine(deviceLocation);
        this.lineMarkerSelectListener.onMarkerSizeChange(this.mMarkerList.size());
        this.isClick = false;
        if (isMapPoint) {
            if (this.mMarkerList.size() > 1 && this.lineMarkerSelectListener.getOration() == 2) {
                int size = this.mMarkerList.size();
                addSmallMakerByMap1(this.mMarkerList.get(size - 2), this.mMarkerList.get(size - 1));
            }
        } else if (this.mMarkerList.size() > 1) {
            int size2 = this.mMarkerList.size();
            addSmallMaker(this.mMarkerList.get(size2 - 2), this.mMarkerList.get(size2 - 1));
        }
        onMarkerClick(mMarker);
    }

    public void addSmallMakerByMap1(Marker marker1, Marker marker2) {
        MapPointLatLng mpl1 = (MapPointLatLng) marker1.getTag();
        MapPointLatLng mpl2 = (MapPointLatLng) marker2.getTag();
        LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(marker1.getPosition(), marker2.getPosition(), 3);
        mpl2.setAngle(getPointAngle(mpl1, mpl2));
        mpl2.yawMode = this.lineMarkerSelectListener.getOration();
        float[] angleArray = {mpl2.showAngle, mpl2.showAngle};
        for (int i = 0; i < latLng.length; i++) {
            MapPointLatLng mpl = new MapPointLatLng();
            mpl.isSelect = true;
            mpl.setAngle(angleArray[i]);
            BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true);
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
            mMarker.setTag(mpl);
            mMarker.setFlat(true);
            this.arrowMarkerList.add(mMarker);
        }
    }

    public void addSmallMaker(Marker marker1, Marker marker2) {
        MapPointLatLng mpl1 = (MapPointLatLng) marker1.getTag();
        MapPointLatLng mpl2 = (MapPointLatLng) marker2.getTag();
        if (mpl1.yawMode != 0) {
            LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(marker1.getPosition(), marker2.getPosition(), 3);
            float[] angleArray = new float[2];
            if (mpl1.yawMode == 2) {
                mpl2.showAngle = getPointAngle(mpl1, mpl2);
                angleArray[0] = mpl2.showAngle;
                angleArray[1] = mpl2.showAngle;
            } else if (mpl1.yawMode == 1) {
                angleArray = this.mapCalcAngle.getAnlgesByRoration(mpl1.showAngle, mpl2.showAngle, mpl2.roration);
            }
            for (int i = 0; i < latLng.length; i++) {
                MapPointLatLng mpl = new MapPointLatLng();
                mpl.isSelect = true;
                mpl.setAngle(angleArray[i]);
                BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true);
                Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
                mMarker.setTag(mpl);
                mMarker.setFlat(true);
                this.arrowMarkerList.add(mMarker);
            }
        }
    }

    public void deleteSmallMaker(Marker deleteMarker) {
        if (this.arrowMarkerList.size() > 0) {
            MapPointLatLng target = (MapPointLatLng) deleteMarker.getTag();
            int index = target.nPos - 1;
            if (index != 0) {
                int n = (index - 1) * 2;
                if (this.arrowMarkerList.size() > n + 1) {
                    Marker m1 = this.arrowMarkerList.get(n);
                    m1.remove();
                    Marker m2 = this.arrowMarkerList.get(n + 1);
                    m2.remove();
                    this.arrowMarkerList.remove(m1);
                    this.arrowMarkerList.remove(m2);
                }
            }
        }
    }

    @Override
    public void updateSmallMarkerAngle(MapPointLatLng target) {
        int index = target.nPos - 1;
        if (index != 0) {
            int n = (index - 1) * 2;
            if (this.arrowMarkerList.size() >= n + 1) {
                MapPointLatLng mpl1 = (MapPointLatLng) this.arrowMarkerList.get(n).getTag();
                MapPointLatLng mpl2 = (MapPointLatLng) this.arrowMarkerList.get(n + 1).getTag();
                MapPointLatLng m1 = (MapPointLatLng) this.mMarkerList.get(index - 1).getTag();
                float[] angleArray = this.mapCalcAngle.getAnlgesByRoration(m1.showAngle, target.showAngle, target.roration);
                mpl1.isSelect = true;
                mpl1.setAngle(angleArray[0]);
                BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl1.showAngle, true);
                this.arrowMarkerList.get(n).setIcon(mBitmapDescriptor);
                this.arrowMarkerList.get(n).setAnchor(0.5f, 0.5f);
                mpl2.isSelect = true;
                mpl2.setAngle(angleArray[1]);
                BitmapDescriptor mBitmapDescriptor2 = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl2.showAngle, true);
                this.arrowMarkerList.get(n + 1).setIcon(mBitmapDescriptor2);
                this.arrowMarkerList.get(n + 1).setAnchor(0.5f, 0.5f);
            }
        }
    }

    @Override
    public void addSmallMarkerByMap(int type) {
        if (type == 0) {
            int i = 0;
            clearAllInterestMarker();
            for (Marker marker : this.mMarkerList) {
                MapPointLatLng p = (MapPointLatLng) marker.getTag();
                clearAllInterestMarkerByMap(p);
                changeViewBySetPoints(marker, p, false, false);
                if (i != 0) {
                    addSmallMakerByMap(this.mMarkerList.get(i - 1), this.mMarkerList.get(i));
                }
                i++;
            }
        }
    }

    public void clearAllInterestMarker() {
        if (this.mSelectMarker != null) {
            MapPointLatLng lastMp = (MapPointLatLng) this.mSelectMarker.getTag();
            if (lastMp.isIntertestPoint) {
                this.mSelectMarker = null;
            }
        }
        for (Marker marker : this.interestMarkerList) {
            marker.remove();
        }
        this.interestMarkerList.clear();
    }

    public void clearAllInterestMarkerByMap(MapPointLatLng tmp) {
        tmp.mInrertestPoint = null;
    }

    public void addSmallMakerByMap(Marker marker1, Marker marker2) {
        MapPointLatLng mpl2 = (MapPointLatLng) marker2.getTag();
        LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(marker1.getPosition(), marker2.getPosition(), 3);
        float[] angleArray = {mpl2.showAngle, mpl2.showAngle};
        for (int i = 0; i < latLng.length; i++) {
            MapPointLatLng mpl = new MapPointLatLng();
            mpl.isSelect = true;
            mpl.setAngle(angleArray[i]);
            BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true);
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
            mMarker.setTag(mpl);
            mMarker.setFlat(true);
            this.arrowMarkerList.add(mMarker);
        }
    }

    @Override
    public void clearAllSmallMarker() {
        for (Marker marker : this.arrowMarkerList) {
            marker.remove();
        }
        this.arrowMarkerList.clear();
        int i = 0;
        for (Marker marker2 : this.mMarkerList) {
            MapPointLatLng p = (MapPointLatLng) marker2.getTag();
            p.isSelect = this.mSelectMarker != null && this.mSelectMarker.getTag() == p;
            changeViewBySetPoints(marker2, p, false, false);
            i++;
        }
    }

    @Override
    public void addOrUpdateSmallMarkerForVideo(int type) {
        clearAllSmallMarker();
        int j = 0;
        for (Marker marker : this.mMarkerList) {
            MapPointLatLng mapPointLatLng = (MapPointLatLng) marker.getTag();
            if (j != 0) {
                MapPointLatLng mpl2 = (MapPointLatLng) this.mMarkerList.get(j).getTag();
                LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(this.mMarkerList.get(j - 1).getPosition(), this.mMarkerList.get(j).getPosition(), 3);
                float[] angleArray = {mpl2.showAngle, mpl2.showAngle};
                for (int i = 0; i < latLng.length; i++) {
                    MapPointLatLng mpl = new MapPointLatLng();
                    mpl.isSelect = true;
                    mpl.setAngle(angleArray[i]);
                    BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true);
                    Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
                    mMarker.setTag(mpl);
                    mMarker.setFlat(true);
                    this.arrowMarkerList.add(mMarker);
                }
            }
            j++;
        }
    }

    @Override
    public void addSmallMarkerByInterest() {
        clearAllSmallMarker();
        int j = 0;
        for (Marker marker : this.mMarkerList) {
            if (j > 0) {
                MapPointLatLng mpl1 = (MapPointLatLng) this.mMarkerList.get(j - 1).getTag();
                MapPointLatLng mpl2 = (MapPointLatLng) this.mMarkerList.get(j).getTag();
                if (mpl2.mInrertestPoint != null) {
                    LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(this.mMarkerList.get(j - 1).getPosition(), this.mMarkerList.get(j).getPosition(), 3);
                    float[] angleArray = new float[2];
                    if (mpl1.mInrertestPoint == null) {
                        angleArray[0] = mpl2.showAngle;
                        angleArray[1] = mpl2.showAngle;
                    } else {
                        angleArray = this.mapCalcAngle.getAnlgesByRoration(mpl1.showAngle, mpl2.showAngle, 0);
                    }
                    for (int i = 0; i < latLng.length; i++) {
                        MapPointLatLng mpl = new MapPointLatLng();
                        mpl.isSelect = true;
                        mpl.setAngle(angleArray[i]);
                        BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true);
                        Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
                        mMarker.setTag(mpl);
                        mMarker.setFlat(true);
                        this.arrowMarkerList.add(mMarker);
                    }
                }
            }
            j++;
        }
    }

    @Override
    public void updateInterestPoint() {
        MapPointLatLng mp = (MapPointLatLng) this.mSelectMarker.getTag();
        if (mp.isIntertestPoint) {
            lightPoint(mp, true);
        }
    }

    public void drawPointLine(LatLng latLngDevice) {
        if (this.gglMapLocationManager != null && this.gglMapLocationManager.getDevLocation() != null) {
            if (this.polylineList != null) {
                for (Polyline polyline : this.polylineList) {
                    polyline.remove();
                }
                this.polylineList.clear();
            }
            for (int i = 0; i < this.mMarkerList.size(); i++) {
                PolylineOptions polylineOptions = new PolylineOptions();
                if (i == 0) {
                    polylineOptions.add(latLngDevice);
                    polylineOptions.add(this.mMarkerList.get(i).getPosition());
                } else {
                    polylineOptions.add(this.mMarkerList.get(i - 1).getPosition());
                    polylineOptions.add(this.mMarkerList.get(i).getPosition());
                }
                polylineOptions.color(this.context.getResources().getColor(this.lineDefaultColor)).zIndex(3.0f);
                polylineOptions.width(4.0f);
                Polyline polyline2 = this.googleMap.addPolyline(polylineOptions);
                polyline2.setPattern(PATTERN_DASHED);
                try {
                    this.polylineList.add(polyline2);
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void changeLine() {
        if (this.mMarkerList != null && this.nPos == -1 && this.polylineList != null && this.polylineList.size() > 0) {
            Polyline c = this.polylineList.get(0);
            List<LatLng> mLatLng = new ArrayList<>();
            mLatLng.add(this.gglMapLocationManager.getDevLocation());
            mLatLng.add(this.mMarkerList.get(0).getPosition());
            c.setPoints(mLatLng);
        }
    }

    @Override
    public void setAiLineMarkByHistory(List<MapPointLatLng> points, List<MapPointLatLng> interests) {
        int i = 0;
        for (MapPointLatLng mpl : interests) {
            i++;
            mpl.nPos = i;
            Marker interestMarker = addInterestMarkerByHistory(mpl);
            interestMarker.setTag(mpl);
            interestMarker.setDraggable(true);
            this.interestMarkerList.add(interestMarker);
        }
        for (MapPointLatLng point : points) {
            Marker mMarker = addPointMarkerByHistory(point);
            if (point.mInrertestPoint != null) {
                findInterestPoint(point, interests);
            }
            mMarker.setTag(point);
            this.mMarkerList.add(mMarker);
            point.distance = 0.0f;
            this.mMapPointList.add(point);
        }
        if (points.size() > 0) {
            drawPointLine(this.gglMapLocationManager.getDevLocation());
        }
    }

    public void findInterestPoint(MapPointLatLng point, List<MapPointLatLng> interests) {
        for (MapPointLatLng mpl : interests) {
            if (point.mInrertestPoint.latitude == mpl.latitude && point.mInrertestPoint.longitude == mpl.longitude) {
                point.mInrertestPoint.nPos = mpl.nPos;
                return;
            }
        }
    }

    @Override
    public void setAiLineMarkByDevice(List<AckGetAiLinePoint> points, List<AckGetAiLinePoint> interestPoints) {
        int res;
        BitmapDescriptor mBitmapDescriptor;
        for (AckGetAiLinePoint point : interestPoints) {
            addInterestByDevice(point);
        }
        for (AckGetAiLinePoint point2 : points) {
            MapPointLatLng mp = new MapPointLatLng();
            mp.yawMode = point2.getYawMode();
            int index = findInterestPoint(point2, interestPoints);
            if (index != -1 && index < this.interestMarkerList.size()) {
                mp.mInrertestPoint = (MapPointLatLng) this.interestMarkerList.get(index).getTag();
            }
            mp.altitude = point2.getAltitude();
            mp.nPos = this.mMarkerList.size() + 1;
            FLatLng fLatLng = GpsCorrect.Earth_To_Mars(point2.getLatitude(), point2.getLongitude());
            LatLng latLng = new LatLng(fLatLng.latitude, fLatLng.longitude);
            mp.longitude = latLng.longitude;
            mp.latitude = latLng.latitude;
            mp.setAngle(point2.getAngle());
            if (point2.getYawMode() == 0) {
                res = R.drawable.x8_ai_line_point_no_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            if (mp.mInrertestPoint != null) {
                mBitmapDescriptor = this.gdCustemMarkerView.createMapPointWithPioView(this.context, res, mp.altitude, mp.nPos, mp.mInrertestPoint.nPos, mp.showAngle, mp.isSelect, false);
            } else {
                mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mp.altitude, mp.nPos, mp.showAngle, mp.isSelect, false);
            }
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 0.64285713f).draggable(false));
            mMarker.setTag(mp);
            mMarker.setFlat(true);
            this.mMarkerList.add(mMarker);
            mp.distance = 0.0f;
            this.mMapPointList.add(mp);
        }
        if (points.size() > 0) {
            drawPointLine(this.gglMapLocationManager.getDevLocation());
        }
    }

    @Override
    public float getDistance(MapPointLatLng points1, MapPointLatLng points2) {
        float distance = (float) GeoTools.getDistance(new LatLng(points1.latitude, points1.longitude), new LatLng(points2.latitude, points2.longitude)).valueInMeters();
        return distance;
    }

    @Override
    public boolean isFarToHome() {
        for (Marker marker : this.mMarkerList) {
            this.gglMapLocationManager.getHomeLocation();
            LatLng hoem = this.gglMapLocationManager.getHomeLocation();
            LatLng latLng = marker.getPosition();
            float distanceHome = (float) GeoTools.getDistance(latLng, hoem).valueInMeters();
            if (distanceHome > 1000.0f) {
                return true;
            }
        }
        return false;
    }

    public void clearMarker() {
        if (this.polylineList != null) {
            for (Polyline polyline : this.polylineList) {
                polyline.remove();
            }
        }
        if (this.limitCircle != null) {
            this.limitCircle.remove();
            this.limitCircle = null;
        }
        this.polylineList.clear();
        this.mMarkerList.clear();
        this.mMapPointList.clear();
        this.interestMarkerList.clear();
        this.arrowMarkerList.clear();
        this.mSelectMarker = null;
    }

    public int getCurrentPointActionRes(int action) {
        switch (action) {
            case -1:
                return -1;
            case 0:
                int id = R.drawable.x8_img_ai_line_action_na1;
                return id;
            case 1:
                int id2 = R.drawable.x8_img_ai_line_action_hover1;
                return id2;
            case 2:
                int id3 = R.drawable.x8_img_ai_line_action_record1;
                return id3;
            case 3:
                int id4 = R.drawable.x8_img_ai_line_action_4x_slow1;
                return id4;
            case 4:
                int id5 = R.drawable.x8_img_ai_line_action_one_photo1;
                return id5;
            case 5:
                int id6 = R.drawable.x8_img_ai_line_action_5s_photo1;
                return id6;
            case 6:
                int id7 = R.drawable.x8_img_ai_line_action_three_photo1;
                return id7;
            default:
                return 0;
        }
    }

    public void onAiLineAddPoint(LatLng latLng, float height, float angle) {
        if (!isFullSize()) {
            if (this.gglMapLocationManager.getDevLocation() != null) {
                LatLng d = this.gglMapLocationManager.getDevLocation();
                LatLng hoem = this.gglMapLocationManager.getHomeLocation();
                float distanceHome = (float) GeoTools.getDistance(latLng, hoem).valueInMeters();
                if (distanceHome <= 1000.0f) {
                    if (isValid(latLng)) {
                        float distance = (float) GeoTools.getDistance(latLng, d).valueInMeters();
                        addPointLatLng(latLng, distance, this.gglMapLocationManager.getDevLocation(), false, angle);
                        return;
                    }
                    String t = String.format(this.context.getString(R.string.x8_ai_fly_lines_point_magin), X8NumberUtil.getDistanceNumberString(10.0f, 0, true));
                    X8ToastUtil.showToast(this.context, t, 0);
                    return;
                }
                String t2 = String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), X8NumberUtil.getDistanceNumberString(1000.0f, 0, true));
                X8ToastUtil.showToast(this.context, t2, 0);
                return;
            }
            return;
        }
        X8ToastUtil.showToast(this.context, this.context.getString(R.string.x8_ai_fly_lines_point_max), 0);
    }

    private void deleteMarker(boolean isMapPoint, LatLng homeLocation) {
        if (!isMapPoint) {
            if (this.mMarkerList.size() != 0) {
                if (this.mSelectMarker != null) {
                    ((MapPointLatLng) this.mSelectMarker.getTag()).isSelect = false;
                }
                this.mSelectMarker = this.mMarkerList.get(this.mMarkerList.size() - 1);
                deleteSmallMaker(this.mSelectMarker);
                removeLinePoint(homeLocation);
                if (this.mMarkerList.size() > 0) {
                    onMarkerClick(this.mMarkerList.get(this.mMarkerList.size() - 1));
                }
            }
        } else if (this.mSelectMarker != null) {
            MapPointLatLng mapPointLatLng = (MapPointLatLng) this.mSelectMarker.getTag();
            if (!mapPointLatLng.isIntertestPoint) {
                if (mapPointLatLng.mInrertestPoint != null) {
                    lightPoint(mapPointLatLng, false);
                }
                resetArrowList(mapPointLatLng);
                removeLinePoint(homeLocation);
                if (this.mMarkerList.size() > 0) {
                    onMarkerClick(this.mMarkerList.get(this.mMarkerList.size() - 1));
                }
                if (mapPointLatLng.yawMode == 0) {
                    addSmallMarkerByInterest();
                    return;
                }
                return;
            }
            removeInterestPoint();
        }
    }

    public void resetArrowList(MapPointLatLng tmp) {
        if (this.arrowMarkerList != null && this.arrowMarkerList.size() > 0 && tmp.yawMode == 2) {
            if (tmp.nPos == this.mMarkerList.size()) {
                int n = ((tmp.nPos - 1) - 1) * 2;
                if (this.arrowMarkerList.size() >= n + 1) {
                    Marker m1 = this.arrowMarkerList.get(n);
                    Marker m2 = this.arrowMarkerList.get(n + 1);
                    m1.remove();
                    m2.remove();
                    this.arrowMarkerList.remove(m1);
                    this.arrowMarkerList.remove(m2);
                }
            } else if (tmp.nPos == 1) {
                Marker m12 = this.arrowMarkerList.get(0);
                Marker m22 = this.arrowMarkerList.get(1);
                m12.remove();
                m22.remove();
                this.arrowMarkerList.remove(m12);
                this.arrowMarkerList.remove(m22);
            } else {
                int n2 = (tmp.nPos - 1) * 2;
                if (this.arrowMarkerList.size() >= n2 + 1) {
                    Marker m13 = this.arrowMarkerList.get(n2);
                    Marker m23 = this.arrowMarkerList.get(n2 + 1);
                    m13.remove();
                    m23.remove();
                    this.arrowMarkerList.remove(m13);
                    this.arrowMarkerList.remove(m23);
                    int n3 = ((tmp.nPos - 1) - 1) * 2;
                    Marker m3 = this.arrowMarkerList.get(n3);
                    Marker m4 = this.arrowMarkerList.get(n3 + 1);
                    m3.remove();
                    m4.remove();
                    this.arrowMarkerList.remove(m3);
                    this.arrowMarkerList.remove(m4);
                    resetSmallMakerByMap(this.mMarkerList.get(tmp.nPos - 2), this.mMarkerList.get(tmp.nPos), n3);
                }
            }
        }
    }

    public void resetSmallMakerByMap(Marker marker1, Marker marker2, int n) {
        MapPointLatLng mpl1 = (MapPointLatLng) marker1.getTag();
        MapPointLatLng mpl2 = (MapPointLatLng) marker2.getTag();
        LatLng[] latLng = this.mapCalcAngle.getLineLatLngInterval(marker1.getPosition(), marker2.getPosition(), 3);
        mpl2.setAngle(getPointAngle(mpl1, mpl2));
        float[] angleArray = {mpl2.showAngle, mpl2.showAngle};
        for (int i = 0; i < latLng.length; i++) {
            MapPointLatLng mpl = new MapPointLatLng();
            mpl.isSelect = true;
            mpl.setAngle(angleArray[i]);
            BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, R.drawable.x8_ai_line_point_small1, mpl.showAngle, true);
            Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng[i]).icon(mBitmapDescriptor).anchor(0.5f, 0.5f).draggable(false));
            mMarker.setTag(mpl);
            mMarker.setFlat(true);
            this.arrowMarkerList.add(n + i, mMarker);
        }
    }

    public void removeLinePoint(LatLng homeLocation) {
        MapPointLatLng removeMapPointLatLng = null;
        Iterator<MapPointLatLng> it = this.mMapPointList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MapPointLatLng mMapPointLatLng = it.next();
            LatLng mLatLng = this.mSelectMarker.getPosition();
            if (mLatLng.longitude == mMapPointLatLng.longitude && mLatLng.latitude == mMapPointLatLng.latitude) {
                removeMapPointLatLng = mMapPointLatLng;
                break;
            }
        }
        if (removeMapPointLatLng != null) {
            this.mMapPointList.remove(removeMapPointLatLng);
        }
        for (Marker marker : this.mMarkerList) {
            if (this.mSelectMarker == marker) {
                break;
            }
        }
        this.mMarkerList.remove(this.mSelectMarker);
        this.mSelectMarker.remove();
        int i = 0;
        for (Marker marker2 : this.mMarkerList) {
            i++;
            MapPointLatLng p = (MapPointLatLng) marker2.getTag();
            p.nPos = i;
            changePointMarker(marker2, p, false);
        }
        drawPointLine(homeLocation);
        this.mSelectMarker = null;
        this.lineMarkerSelectListener.onMarkerSizeChange(this.mMarkerList.size());
    }

    public void removeInterestPoint() {
        MapPointLatLng tmp = (MapPointLatLng) this.mSelectMarker.getTag();
        int i = 0;
        for (MapPointLatLng mpl : this.mMapPointList) {
            if (mpl.mInrertestPoint != null && tmp == mpl.mInrertestPoint) {
                mpl.mInrertestPoint = null;
                changePointMarker(this.mMarkerList.get(i), mpl, false);
            }
            i++;
        }
        this.interestMarkerList.remove(this.mSelectMarker);
        this.mSelectMarker.remove();
        int i2 = 0;
        for (Marker marker : this.interestMarkerList) {
            i2++;
            MapPointLatLng p = (MapPointLatLng) marker.getTag();
            p.nPos = i2;
            changePointMarker(marker, p, false);
        }
        this.mSelectMarker = null;
        if (this.lineMarkerSelectListener != null) {
            this.lineMarkerSelectListener.onInterestSizeEnable(this.interestMarkerList.size() < 20);
        }
    }

    public void onMapClickForAiLine(LatLng latLng) {
        if (isOnMapClickValid()) {
            onAiLineAddPoint(latLng);
        }
    }

    public void onAiLineAddPoint(LatLng latLng) {
        if (!isFullSize()) {
            if (this.gglMapLocationManager.getDevLocation() != null) {
                LatLng d = this.gglMapLocationManager.getDevLocation();
                LatLng hoem = this.gglMapLocationManager.getHomeLocation();
                float distanceHome = (float) GeoTools.getDistance(latLng, hoem).valueInMeters();
                if (distanceHome <= 1000.0f) {
                    if (isValid(latLng)) {
                        float distance = (float) GeoTools.getDistance(latLng, d).valueInMeters();
                        addPointLatLng(latLng, distance, this.gglMapLocationManager.getDevLocation(), true, -1.0f);
                        return;
                    }
                    String t = String.format(this.context.getString(R.string.x8_ai_fly_lines_point_magin), X8NumberUtil.getDistanceNumberString(10.0f, 0, true));
                    X8ToastUtil.showToast(this.context, t, 0);
                    return;
                }
                String t2 = String.format(this.context.getString(R.string.x8_ai_fly_follow_point_to_point_far), X8NumberUtil.getDistanceNumberString(1000.0f, 0, true));
                X8ToastUtil.showToast(this.context, t2, 0);
                return;
            }
            return;
        }
        X8ToastUtil.showToast(this.context, this.context.getString(R.string.x8_ai_fly_lines_point_max), 0);
    }

    public void changePointMarker(Marker marker, MapPointLatLng mpl, boolean isRelation) {
        int res;
        int res2;
        if (mpl.isMapPoint) {
            if (mpl.isIntertestPoint) {
                if (mpl.isSelect) {
                    res2 = R.drawable.x8_img_ai_line_inreterst_max2;
                } else {
                    res2 = R.drawable.x8_img_ai_line_inreterst_max1;
                }
                BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createMapPioView(this.context, res2, mpl.altitude, mpl.nPos, mpl.isSelect, isRelation);
                marker.setIcon(mBitmapDescriptor);
                marker.setAnchor(0.5f, 0.73802954f);
                return;
            } else if (mpl.mInrertestPoint != null) {
                if (mpl.isSelect) {
                    res = R.drawable.x8_ai_line_point_with_angle2;
                } else {
                    res = R.drawable.x8_ai_line_point_with_angle1;
                }
                BitmapDescriptor mBitmapDescriptor2 = this.gdCustemMarkerView.createMapPointWithPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.mInrertestPoint.nPos, mpl.showAngle, mpl.isSelect, isRelation);
                marker.setIcon(mBitmapDescriptor2);
                marker.setAnchor(0.5f, 0.5f);
                return;
            } else {
                changeAngleOrOnAngle(marker, mpl, isRelation);
                return;
            }
        }
        changeAngleOrOnAngle(marker, mpl, isRelation);
    }

    public void changeAngleOrOnAngle(Marker marker, MapPointLatLng mpl, boolean isRelation) {
        int res;
        BitmapDescriptor mBitmapDescriptor;
        int res2;
        if (mpl.yawMode == 1 || mpl.yawMode == 2) {
            if (mpl.isSelect) {
                res = R.drawable.x8_ai_line_point_with_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, isRelation);
        } else {
            if (mpl.isSelect) {
                res2 = R.drawable.x8_ai_line_point_no_angle2;
            } else {
                res2 = R.drawable.x8_ai_line_point_no_angle1;
            }
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, res2, mpl.altitude, mpl.nPos, mpl.isSelect, isRelation);
        }
        marker.setIcon(mBitmapDescriptor);
        marker.setAnchor(0.5f, 0.64285713f);
    }

    public void changeViewBySetPoints(Marker marker, MapPointLatLng mpl, boolean isRelation, boolean isRun) {
        int res;
        int res2;
        float anchorY;
        int res3;
        BitmapDescriptor mBitmapDescriptor;
        int res4;
        float anchorY2;
        int res5;
        BitmapDescriptor mBitmapDescriptor2;
        int res6;
        if (mpl.isMapPoint) {
            if (mpl.isIntertestPoint) {
                changePointMarker(marker, mpl, false);
            } else if (mpl.yawMode == 0) {
                if (mpl.mInrertestPoint != null) {
                    anchorY2 = 0.5f;
                    if (mpl.isSelect) {
                        res6 = R.drawable.x8_ai_line_point_with_angle2;
                    } else if (isRun) {
                        res6 = R.drawable.x8_ai_line_point_run_angle1;
                    } else {
                        res6 = R.drawable.x8_ai_line_point_with_angle1;
                    }
                    mBitmapDescriptor2 = this.gdCustemMarkerView.createMapPointWithPioView(this.context, res6, mpl.altitude, mpl.nPos, mpl.mInrertestPoint.nPos, mpl.showAngle, mpl.isSelect, isRelation);
                } else {
                    anchorY2 = 0.64285713f;
                    if (mpl.isSelect) {
                        res5 = R.drawable.x8_ai_line_point_no_angle2;
                    } else if (isRun) {
                        res5 = R.drawable.x8_ai_line_point_run_no_angle1;
                    } else {
                        res5 = R.drawable.x8_ai_line_point_no_angle1;
                    }
                    mBitmapDescriptor2 = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, res5, mpl.altitude, mpl.nPos, mpl.isSelect, isRelation);
                    marker.setIcon(mBitmapDescriptor2);
                    marker.setAnchor(0.5f, 0.64285713f);
                }
                marker.setIcon(mBitmapDescriptor2);
                marker.setAnchor(0.5f, anchorY2);
            } else {
                if (mpl.mInrertestPoint != null) {
                    anchorY = 0.5f;
                    if (mpl.isSelect) {
                        res4 = R.drawable.x8_ai_line_point_with_angle2;
                    } else if (isRun) {
                        res4 = R.drawable.x8_ai_line_point_run_angle1;
                    } else {
                        res4 = R.drawable.x8_ai_line_point_with_angle1;
                    }
                    mBitmapDescriptor = this.gdCustemMarkerView.createMapPointWithPioView(this.context, res4, mpl.altitude, mpl.nPos, mpl.mInrertestPoint.nPos, mpl.showAngle, mpl.isSelect, isRelation);
                } else {
                    anchorY = 0.64285713f;
                    if (mpl.isSelect) {
                        res3 = R.drawable.x8_ai_line_point_with_angle2;
                    } else if (isRun) {
                        res3 = R.drawable.x8_ai_line_point_run_angle1;
                    } else {
                        res3 = R.drawable.x8_ai_line_point_with_angle1;
                    }
                    mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res3, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, isRelation);
                }
                marker.setIcon(mBitmapDescriptor);
                marker.setAnchor(0.5f, anchorY);
            }
        } else if (mpl.yawMode == 0) {
            if (mpl.isSelect) {
                res2 = R.drawable.x8_ai_line_point_no_angle2;
            } else if (isRun) {
                res2 = R.drawable.x8_ai_line_point_run_no_angle1;
            } else {
                res2 = R.drawable.x8_ai_line_point_no_angle1;
            }
            BitmapDescriptor mBitmapDescriptor3 = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, res2, mpl.altitude, mpl.nPos, mpl.isSelect, isRelation);
            marker.setIcon(mBitmapDescriptor3);
            marker.setAnchor(0.5f, 0.64285713f);
        } else {
            if (mpl.isSelect) {
                res = R.drawable.x8_ai_line_point_with_angle2;
            } else if (isRun) {
                res = R.drawable.x8_ai_line_point_run_angle1;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle1;
            }
            BitmapDescriptor mBitmapDescriptor4 = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, isRelation);
            marker.setIcon(mBitmapDescriptor4);
            marker.setAnchor(0.5f, 0.64285713f);
        }
    }

    public void changeViewByRunning(Marker marker, MapPointLatLng mpl) {
        int res;
        int actionRes;
        int res2;
        int actionRes2;
        if (!mpl.isIntertestPoint) {
            if (mpl.mInrertestPoint != null) {
                if (mpl.yawMode == 0) {
                    res2 = R.drawable.x8_ai_line_point_with_angle2;
                } else {
                    res2 = R.drawable.x8_ai_line_point_with_angle2;
                }
                if (SPStoreManager.getInstance().getBoolean("isExecuteCurveProcess", false)) {
                    actionRes2 = -1;
                } else {
                    actionRes2 = getCurrentPointActionRes(mpl.action);
                }
                BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointEventWithPioView(this.context, actionRes2, res2, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false);
                marker.setIcon(mBitmapDescriptor);
                marker.setAnchor(0.5f, 0.6846361f);
                return;
            }
            if (mpl.yawMode == 0) {
                res = R.drawable.x8_ai_line_point_no_angle2;
            } else {
                res = R.drawable.x8_ai_line_point_with_angle2;
            }
            if (SPStoreManager.getInstance().getBoolean("isExecuteCurveProcess", false)) {
                actionRes = -1;
            } else {
                actionRes = getCurrentPointActionRes(mpl.action);
            }
            BitmapDescriptor mBitmapDescriptor2 = this.gdCustemMarkerView.createPointEventNoPioView(this.context, actionRes, res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false);
            marker.setIcon(mBitmapDescriptor2);
            marker.setAnchor(0.5f, 0.7962384f);
        }
    }

    public void changeLineSmallMarkerByRunning(Marker marker1, Marker marker2, float angle, int type) {
        int res = 0;
        if (type == 0) {
            res = R.drawable.x8_ai_line_point_small1;
        } else if (type == 1) {
            res = R.drawable.x8_ai_line_point_small3;
        } else if (type == 2) {
            res = R.drawable.x8_ai_line_point_small2;
        }
        BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, res, angle, ((MapPointLatLng) marker2.getTag()).isSelect);
        marker1.setIcon(mBitmapDescriptor);
        marker1.setAnchor(0.5f, 0.5f);
        BitmapDescriptor mBitmapDescriptor2 = this.gdCustemMarkerView.createPointWithSmallArrow(this.context, res, angle, ((MapPointLatLng) marker2.getTag()).isSelect);
        marker2.setIcon(mBitmapDescriptor2);
        marker2.setAnchor(0.5f, 0.5f);
    }

    public Marker addPointMarker(boolean isMapPoint, LatLng latLng, MapPointLatLng mpl, float h, float angle) {
        BitmapDescriptor mBitmapDescriptor;
        float anchorY;
        if (isMapPoint) {
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, R.drawable.x8_ai_line_point_no_angle1, h, mpl.nPos, mpl.isSelect, false);
            mpl.altitude = h;
            mpl.isMapPoint = isMapPoint;
            anchorY = 0.64285713f;
        } else {
            mpl.angle = angle;
            mpl.showAngle = angle;
            int res = R.drawable.x8_ai_line_point_with_angle1;
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false);
            mpl.altitude = h;
            mpl.isMapPoint = isMapPoint;
            anchorY = 0.5f;
        }
        Marker mMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, anchorY).draggable(false));
        mMarker.setFlat(true);
        return mMarker;
    }

    public Marker addInterestMarker(LatLng latLng, float h, MapPointLatLng mpl) {
        int res = R.drawable.x8_img_ai_line_inreterst_max1;
        BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createCustomMarkerView(this.context, res, h, mpl.nPos);
        Marker interestMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(mBitmapDescriptor).anchor(0.5f, 1.0f).draggable(false));
        interestMarker.setFlat(true);
        return interestMarker;
    }

    public Marker addInterestMarkerByHistory(MapPointLatLng mpl) {
        int res;
        if (mpl.isSelect) {
            res = R.drawable.x8_img_ai_line_inreterst_max2;
        } else {
            res = R.drawable.x8_img_ai_line_inreterst_max1;
        }
        BitmapDescriptor mBitmapDescriptor = this.gdCustemMarkerView.createMapPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.isSelect, false);
        Marker interestMarker = this.googleMap.addMarker(new MarkerOptions().position(new LatLng(mpl.latitude, mpl.longitude)).icon(mBitmapDescriptor).anchor(0.5f, 0.73802954f).draggable(false));
        interestMarker.setFlat(true);
        return interestMarker;
    }

    public Marker addPointMarkerByHistory(MapPointLatLng mpl) {
        BitmapDescriptor mBitmapDescriptor;
        float anchorY;
        if (this.lineMarkerSelectListener.getOration() == 0) {
            if (mpl.mInrertestPoint != null) {
                mpl.setAngle(getPointAngle(mpl, mpl.mInrertestPoint));
                int res = R.drawable.x8_ai_line_point_with_angle1;
                mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false);
                anchorY = 0.64285713f;
            } else {
                mBitmapDescriptor = this.gdCustemMarkerView.createMapPointNoAngleNoPioView(this.context, R.drawable.x8_ai_line_point_no_angle1, mpl.altitude, mpl.nPos, mpl.isSelect, false);
                anchorY = 0.64285713f;
            }
        } else {
            int res2 = R.drawable.x8_ai_line_point_with_angle1;
            mBitmapDescriptor = this.gdCustemMarkerView.createMapPointAngleNoPioView(this.context, res2, mpl.altitude, mpl.nPos, mpl.showAngle, mpl.isSelect, false);
            anchorY = 0.64285713f;
        }
        Marker marker = this.googleMap.addMarker(new MarkerOptions().position(new LatLng(mpl.latitude, mpl.longitude)).icon(mBitmapDescriptor).anchor(0.5f, anchorY).draggable(false));
        marker.setFlat(true);
        return marker;
    }

    @Override
    public void addSmallMakerByHistory() {
        int i = 0;
        for (Marker marker : this.mMarkerList) {
            MapPointLatLng mapPointLatLng = (MapPointLatLng) marker.getTag();
            if (i != 0) {
                addSmallMakerByMap(this.mMarkerList.get(i - 1), this.mMarkerList.get(i));
            }
            i++;
        }
    }
}

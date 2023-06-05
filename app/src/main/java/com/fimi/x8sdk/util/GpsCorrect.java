package com.fimi.x8sdk.util;

import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.map.MapType;

/* loaded from: classes2.dex */
public class GpsCorrect {
    static final double a = 6378245.0d;
    static final double ee = 0.006693421622965943d;
    static final double pi = 3.141592653589793d;

    public static void transform(double wgLat, double wgLon, double[] latlng) {
        if (outOfChina(wgLat, wgLon)) {
            latlng[0] = wgLat;
            latlng[1] = wgLon;
            return;
        }
        double dLat = transformLat(wgLon - 105.0d, wgLat - 35.0d);
        double dLon = transformLon(wgLon - 105.0d, wgLat - 35.0d);
        double radLat = (wgLat / 180.0d) * pi;
        double magic = Math.sin(radLat);
        double magic2 = 1.0d - ((ee * magic) * magic);
        double sqrtMagic = Math.sqrt(magic2);
        double dLat2 = (180.0d * dLat) / ((6335552.717000426d / (magic2 * sqrtMagic)) * pi);
        double dLon2 = (180.0d * dLon) / (((a / sqrtMagic) * Math.cos(radLat)) * pi);
        latlng[0] = wgLat + dLat2;
        latlng[1] = wgLon + dLon2;
    }

    private static boolean outOfChina(double lat, double lon) {
        return lon < 72.004d || lon > 137.8347d || lat < 0.8293d || lat > 55.8271d;
    }

    private static double transformLat(double x, double y) {
        double ret = (-100.0d) + (2.0d * x) + (3.0d * y) + (0.2d * y * y) + (0.1d * x * y) + (0.2d * Math.sqrt(Math.abs(x)));
        return ret + ((((20.0d * Math.sin((6.0d * x) * pi)) + (20.0d * Math.sin((2.0d * x) * pi))) * 2.0d) / 3.0d) + ((((20.0d * Math.sin(pi * y)) + (40.0d * Math.sin((y / 3.0d) * pi))) * 2.0d) / 3.0d) + ((((160.0d * Math.sin((y / 12.0d) * pi)) + (320.0d * Math.sin((pi * y) / 30.0d))) * 2.0d) / 3.0d);
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0d + x + (2.0d * y) + (0.1d * x * x) + (0.1d * x * y) + (0.1d * Math.sqrt(Math.abs(x)));
        return ret + ((((20.0d * Math.sin((6.0d * x) * pi)) + (20.0d * Math.sin((2.0d * x) * pi))) * 2.0d) / 3.0d) + ((((20.0d * Math.sin(pi * x)) + (40.0d * Math.sin((x / 3.0d) * pi))) * 2.0d) / 3.0d) + ((((150.0d * Math.sin((x / 12.0d) * pi)) + (300.0d * Math.sin((x / 30.0d) * pi))) * 2.0d) / 3.0d);
    }

    public static FLatLng Earth_To_Mars(double srclat, double srclon) {
        if (GlobalConfig.getInstance().getMapType() == MapType.AMap) {
            if (GlobalConfig.getInstance().isRectification()) {
                double dLat = transformLat(srclon - 105.0d, srclat - 35.0d);
                double dLon = transformLon(srclon - 105.0d, srclat - 35.0d);
                double radLat = (srclat / 180.0d) * pi;
                double magic = Math.sin(radLat);
                double magic2 = 1.0d - ((ee * magic) * magic);
                double sqrtMagic = Math.sqrt(magic2);
                double dLat2 = (180.0d * dLat) / ((6335552.717000426d / (magic2 * sqrtMagic)) * pi);
                double latlat = srclat + dLat2;
                double latlon = srclon + ((180.0d * dLon) / (((a / sqrtMagic) * Math.cos(radLat)) * pi));
                FLatLng latitudelongitude = new FLatLng(latlat, latlon);
                return latitudelongitude;
            }
            FLatLng latitudelongitude2 = new FLatLng(srclat, srclon);
            return latitudelongitude2;
        }
        FLatLng latitudelongitude3 = new FLatLng(srclat, srclon);
        return latitudelongitude3;
    }

    public static FLatLng Mars_To_Earth0(double srclat, double srclon) {
        if (GlobalConfig.getInstance().getMapType() == MapType.AMap) {
            if (GlobalConfig.getInstance().isRectification()) {
                double dLat = transformLat(srclon - 105.0d, srclat - 35.0d);
                double dLon = transformLon(srclon - 105.0d, srclat - 35.0d);
                double radLat = (srclat / 180.0d) * pi;
                double magic = Math.sin(radLat);
                double magic2 = 1.0d - ((ee * magic) * magic);
                double sqrtMagic = Math.sqrt(magic2);
                double dLat2 = (180.0d * dLat) / ((6335552.717000426d / (magic2 * sqrtMagic)) * pi);
                double latlat = srclat - dLat2;
                double latlon = srclon - ((180.0d * dLon) / (((a / sqrtMagic) * Math.cos(radLat)) * pi));
                return new FLatLng(latlat, latlon);
            }
            return new FLatLng(srclat, srclon);
        }
        return new FLatLng(srclat, srclon);
    }

    static double Get_Distance2(double lng0, double lat0, double lng1, double lat1) {
        double dlat = lat0 - lat1;
        double dlng = lng0 - lng1;
        float tmp = (float) (Math.abs(lat0) * 0.0174532925d);
        float scalelongdown0 = (float) Math.cos(tmp);
        double tmp0 = ((float) dlng) * scalelongdown0;
        double tmp1 = (float) dlat;
        return Math.sqrt(((1.0E7d * tmp0 * 1.0E7d * tmp0) + (1.0E7d * tmp1 * 1.0E7d * tmp1)) * 1.2392029762268066d);
    }

    public static FLatLng Mars_To_Earth(double precision, double srclat, double srclon) {
        double delte_l;
        new FLatLng();
        FLatLng tmpLatitudelongitude1 = new FLatLng();
        FLatLng dstlatlon = new FLatLng();
        tmpLatitudelongitude1.latitude = srclat;
        tmpLatitudelongitude1.longitude = srclon;
        FLatLng tmplLatitudelongitude = Mars_To_Earth0(tmpLatitudelongitude1.latitude, tmpLatitudelongitude1.longitude);
        FLatLng tmpLatitudelongitude12 = Earth_To_Mars(tmplLatitudelongitude.latitude, tmplLatitudelongitude.longitude);
        do {
            double deltelon = srclon - tmpLatitudelongitude12.longitude;
            double deltelat = srclat - tmpLatitudelongitude12.latitude;
            dstlatlon.longitude = tmplLatitudelongitude.longitude + deltelon;
            dstlatlon.latitude = tmplLatitudelongitude.latitude + deltelat;
            tmplLatitudelongitude.longitude = dstlatlon.longitude;
            tmplLatitudelongitude.latitude = dstlatlon.latitude;
            tmpLatitudelongitude12 = Earth_To_Mars(tmplLatitudelongitude.latitude, tmplLatitudelongitude.longitude);
            delte_l = Get_Distance2(srclon, srclat, tmpLatitudelongitude12.longitude, tmpLatitudelongitude12.latitude);
        } while (precision < delte_l);
        return dstlatlon;
    }
}

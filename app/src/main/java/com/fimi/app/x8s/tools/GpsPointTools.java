package com.fimi.app.x8s.tools;

import java.lang.reflect.Array;


public class GpsPointTools {
    public double getDistance2D(double lat0, double lng0, double lat1, double lng1) {
        double dLat = lat0 - lat1;
        double dLng = lng0 - lng1;
        if (dLng > 180.0d) {
            dLng -= 360.0d;
        } else if (dLng < -180.0d) {
            dLng += 360.0d;
        }
        double dLat2 = dLat * 0.0174532925d;
        double dLng2 = dLng * 0.0174532925d;
        double sLat = Math.sin(0.5d * dLat2) * Math.sin(0.5d * dLat2);
        double sLng = Math.sin(0.5d * dLng2) * Math.sin(0.5d * dLng2);
        double cLat = Math.cos(0.0174532925d * lat0) * Math.cos(0.0174532925d * lat1);
        double sqVol = Math.sqrt((sLng * cLat) + sLat);
        return 2.0d * 6378137.0d * Math.asin(sqVol);
    }

    public double[][] gpsPointDrawArc(double latA, double lngA, double latB, double lngB, double latO, double lngO) {
        double dAngle;
        double[][] PointA_B = (double[][]) Array.newInstance(Double.TYPE, 50, 2);
        PointA_B[0][0] = latA;
        PointA_B[0][1] = lngA;
        PointA_B[49][0] = latB;
        PointA_B[49][1] = lngB;
        double startAngle = getDirectionAngle(latO, lngO, latA, lngA);
        double endAngle = getDirectionAngle(latO, lngO, latB, lngB);
        double R1 = Math.sqrt(((latO - latA) * (latO - latA)) + ((lngO - lngA) * (lngO - lngA)));
        double R2 = Math.sqrt(((latO - latB) * (latO - latB)) + ((lngO - lngB) * (lngO - lngB)));
        double Rt = (R1 - R2) / 50;
        if (endAngle - startAngle > 0.0d) {
            dAngle = -(((360.0d - endAngle) + startAngle) / 56);
        } else {
            dAngle = (endAngle - startAngle) / 56;
        }
        for (int i = 1; i < 49; i++) {
            double tAngle = startAngle + (i * dAngle);
            if (tAngle > 360.0d) {
                tAngle -= 360.0d;
            }
            PointA_B[i][0] = ((R1 - (i * Rt)) * Math.sin((3.141592653589793d * tAngle) / 180.0d)) + latO;
            PointA_B[i][1] = ((R1 - (i * Rt)) * Math.cos((3.141592653589793d * tAngle) / 180.0d)) + lngO;
        }
        return PointA_B;
    }

    public double getDirectionAngle(double latA, double lngA, double latB, double lngB) {
        double bearing;
        double A_LngDeg = Math.ceil(lngA);
        double A_LngMin = Math.ceil((lngA - A_LngDeg) * 60.0d);
        double d = ((lngA - A_LngDeg) - (A_LngMin / 60.0d)) * 3600.0d;
        double A_LatDeg = Math.ceil(latA);
        double A_LatMin = Math.ceil((latA - A_LatDeg) * 60.0d);
        double d2 = ((latA - A_LatDeg) - (A_LatMin / 60.0d)) * 3600.0d;
        double A_RadLng = (3.141592653589793d * lngA) / 180.0d;
        double A_RadLat = (3.141592653589793d * latA) / 180.0d;
        double A_Ec = 6356725.0d + (((6378137.0d - 6356725.0d) * (90.0d - latA)) / 90.0d);
        double A_Ed = A_Ec * Math.cos(A_RadLat);
        double B_LngDeg = Math.ceil(lngB);
        double B_LngMin = Math.ceil((lngB - B_LngDeg) * 60.0d);
        double d3 = ((lngB - B_LngDeg) - (B_LngMin / 60.0d)) * 3600.0d;
        double B_LatDeg = Math.ceil(latB);
        double B_LatMin = Math.ceil((latB - B_LatDeg) * 60.0d);
        double d4 = ((latB - B_LatDeg) - (B_LatMin / 60.0d)) * 3600.0d;
        double B_RadLng = (3.141592653589793d * lngB) / 180.0d;
        double B_RadLat = (3.141592653589793d * latB) / 180.0d;
        double B_Ec = 6356725.0d + (((6378137.0d - 6356725.0d) * (90.0d - latB)) / 90.0d);
        double cos = B_Ec * Math.cos(B_RadLat);
        double dx = (B_RadLng - A_RadLng) * A_Ed;
        double dy = (B_RadLat - A_RadLat) * A_Ec;
        double dLng = lngB - lngA;
        double dLat = latB - latA;
        double angle = (Math.atan(Math.abs(dx / dy)) * 180.0d) / 3.141592653589793d;
        if (dLng > 0.0d && dLat <= 0.0d) {
            bearing = (90.0d - angle) + 90.0d;
        } else if (dLng <= 0.0d && dLat < 0.0d) {
            bearing = angle + 180.0d;
        } else if (dLng < 0.0d && dLat >= 0.0d) {
            bearing = (90.0d - angle) + 270.0d;
        } else {
            bearing = angle;
        }
        double bearing2 = (360.0d - bearing) + 90.0d;
        if (bearing2 > 360.0d) {
            return bearing2 - 360.0d;
        }
        return bearing2;
    }

    public double[] getSymmetryPoint(double latA, double lngA, double latO, double lngO) {
        return new double[]{(2.0d * latO) - latA, (2.0d * lngO) - lngA};
    }
}

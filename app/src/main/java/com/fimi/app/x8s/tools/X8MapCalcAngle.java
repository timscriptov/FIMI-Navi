package com.fimi.app.x8s.tools;

import com.google.android.gms.maps.model.LatLng;


public class X8MapCalcAngle {
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        return (toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude);
    }

    private float getSpecialAngle(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.latitude - fromPoint.latitude > 0.0d) {
            return 90.0f;
        }
        return 180.0f;
    }

    private float toSceenAngle(double angle, LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude - fromPoint.longitude >= 0.0d) {
            float a = (int) Math.round(90.0d - angle);
            return a;
        }
        float a2 = (int) Math.round(180.0d + (90.0d - angle));
        return a2;
    }

    public float getAngle(LatLng fromPoint, LatLng toPoint) {
        double angle;
        double k = getSlope(fromPoint, toPoint);
        if (k != Double.MAX_VALUE) {
            angle = (Math.atan(k) * 180.0d) / 3.141592653589793d;
        } else {
            angle = getSpecialAngle(fromPoint, toPoint);
        }
        return toSceenAngle(angle, fromPoint, toPoint);
    }

    public LatLng[] getLineLatLngInterval(LatLng fromPoint, LatLng toPoint, int count) {
        if (count < 2) {
            return null;
        }
        double k = getSlope(fromPoint, toPoint);
        LatLng[] latLng = new LatLng[count - 1];
        if (k != Double.MAX_VALUE) {
            double d = (toPoint.longitude - fromPoint.longitude) / count;
            for (int i = 1; i < count; i++) {
                double lng = fromPoint.longitude + (i * d);
                double lat = fromPoint.latitude - ((fromPoint.longitude - lng) * k);
                LatLng mLatLng = new LatLng(lat, lng);
                latLng[i - 1] = mLatLng;
            }
            return latLng;
        }
        double d2 = (toPoint.latitude - fromPoint.latitude) / count;
        for (int i2 = 1; i2 < count; i2++) {
            double lat2 = fromPoint.latitude + (i2 * d2);
            latLng[i2 - 1] = new LatLng(lat2, toPoint.longitude);
        }
        return latLng;
    }

    public float[] getAnlgesByRoration(float angle1, float angle2, int roration) {
        float angle22;
        float angle12;
        float angle13 = (angle1 + 360.0f) % 360.0f;
        float angle23 = (angle2 + 360.0f) % 360.0f;
        if (roration == 1) {
            float tempAngle = angle23 - angle13;
            if (tempAngle < 0.0f) {
                tempAngle += 360.0f;
            }
            float tempAngle2 = tempAngle / 3.0f;
            angle12 = (angle13 + tempAngle2) % 360.0f;
            angle22 = (((tempAngle2 * 2.0f) / 3.0f) + angle12) % 360.0f;
        } else if (roration == 2) {
            float tempAngle3 = angle13 - angle23;
            if (tempAngle3 < 0.0f) {
                tempAngle3 += 360.0f;
            }
            float tempAngle4 = tempAngle3 / 3.0f;
            angle22 = (angle23 + tempAngle4) % 360.0f;
            angle12 = (((tempAngle4 * 2.0f) / 3.0f) + angle22) % 360.0f;
        } else {
            float tempAngle5 = Math.abs(angle23 - angle13);
            if (tempAngle5 > 180.0f) {
                tempAngle5 = 360.0f - tempAngle5;
            }
            float tempAngle6 = tempAngle5 / 3.0f;
            if (angle23 > angle13) {
                angle12 = (angle13 + tempAngle6) % 360.0f;
                angle22 = (((tempAngle6 * 2.0f) / 3.0f) + angle12) % 360.0f;
            } else if (Math.abs(angle23 - angle13) > 180.0f) {
                angle12 = (angle13 + tempAngle6) % 360.0f;
                angle22 = (((tempAngle6 * 2.0f) / 3.0f) + angle12) % 360.0f;
            } else {
                angle22 = (angle23 + tempAngle6) % 360.0f;
                angle12 = (((tempAngle6 * 2.0f) / 3.0f) + angle22) % 360.0f;
            }
        }
        float[] angleArray = {switchScreenAngle2DroneAngle(angle12), switchScreenAngle2DroneAngle(angle22)};
        return angleArray;
    }

    public float switchScreenAngle2DroneAngle(float angle) {
        if (0.0f > angle || angle > 180.0f) {
            return angle - 360.0f;
        }
        return angle;
    }

    public float getAngle2(LatLng fromPoint, LatLng toPoint) {
        double angle;
        double angle2;
        double k = getSlope(fromPoint, toPoint);
        if (k != Double.MAX_VALUE) {
            angle = (Math.atan(k) * 180.0d) / 3.141592653589793d;
        } else {
            angle = getSpecialAngle(fromPoint, toPoint);
        }
        if (toPoint.longitude - fromPoint.longitude >= 0.0d) {
            if (angle > 0.0d) {
                angle2 = 360.0d - angle;
            } else {
                angle2 = -angle;
            }
        } else {
            angle2 = 180.0d - angle;
        }
        return (float) angle2;
    }
}

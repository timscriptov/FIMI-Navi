package com.fimi.x8sdk.util;

import java.util.Locale;


public class Length {
    private double lengthInMeters;

    public Length(double lengthInMeters) {
        set(lengthInMeters);
    }

    public double valueInMeters() {
        return this.lengthInMeters;
    }

    public void set(double lengthInMeters) {
        this.lengthInMeters = lengthInMeters;
    }

    public String toString() {
        return this.lengthInMeters >= 1000.0d ? String.format(Locale.US, "%2.1f km", Double.valueOf(this.lengthInMeters / 1000.0d)) : this.lengthInMeters >= 1.0d ? String.format(Locale.US, "%2.1f m", Double.valueOf(this.lengthInMeters)) : this.lengthInMeters >= 0.001d ? String.format(Locale.US, "%2.1f mm", Double.valueOf(this.lengthInMeters * 1000.0d)) : this.lengthInMeters + " m";
    }

    public boolean equals(Object o) {
        return (o instanceof Length) && this.lengthInMeters == ((Length) o).lengthInMeters;
    }
}

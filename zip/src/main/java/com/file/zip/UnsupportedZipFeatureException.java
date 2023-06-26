package com.file.zip;

import androidx.annotation.NonNull;

import java.util.zip.ZipException;

public class UnsupportedZipFeatureException extends ZipException {
    private static final long serialVersionUID = 4430521921766595597L;
    private final ZipEntry entry;
    private final Feature reason;

    public UnsupportedZipFeatureException(Feature reason, @NonNull ZipEntry entry) {
        super("unsupported feature " + reason + " used in entry " + entry.getName());
        this.reason = reason;
        this.entry = entry;
    }

    public Feature getFeature() {
        return this.reason;
    }

    public ZipEntry getEntry() {
        return this.entry;
    }

    public static class Feature {
        public static final Feature ENCRYPTION = new Feature("encryption");
        public static final Feature METHOD = new Feature("compression method");
        public static final Feature DATA_DESCRIPTOR = new Feature("data descriptor");
        private final String name;

        private Feature(String name) {
            this.name = name;
        }

        @NonNull
        public String toString() {
            return this.name;
        }
    }
}

package com.file.zip;

import androidx.annotation.NonNull;

public enum Zip64Mode {
    Always,
    Never,
    AsNeeded;

    @NonNull
    public static Zip64Mode[] valuesCustom() {
        Zip64Mode[] valuesCustom = values();
        int length = valuesCustom.length;
        Zip64Mode[] zip64ModeArr = new Zip64Mode[length];
        System.arraycopy(valuesCustom, 0, zip64ModeArr, 0, length);
        return zip64ModeArr;
    }
}

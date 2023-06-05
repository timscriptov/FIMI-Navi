package com.fimi.widget.sticklistview.util;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class PinyinComparatorCountry implements Comparator<CountryCodeEntity> {
    @Override
    public int compare(@NonNull CountryCodeEntity o1, CountryCodeEntity o2) {
        if (o1.getSortLetter().equals("@") || o2.getSortLetter().equals("#")) {
            return -1;
        }
        if (o1.getSortLetter().equals("#") || o2.getSortLetter().equals("@")) {
            return 1;
        }
        return o1.getSortLetter().compareTo(o2.getSortLetter());
    }
}

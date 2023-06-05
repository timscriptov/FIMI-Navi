package com.fimi.widget.sticklistview;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class PinyinComparator implements Comparator<SortModel> {
    @Override
    public int compare(@NonNull SortModel o1, SortModel o2) {
        if (o1.getSortLetter().equals("@") || o2.getSortLetter().equals("#")) {
            return -1;
        }
        if (o1.getSortLetter().equals("#") || o2.getSortLetter().equals("@")) {
            return 1;
        }
        return o1.getSortLetter().compareTo(o2.getSortLetter());
    }
}

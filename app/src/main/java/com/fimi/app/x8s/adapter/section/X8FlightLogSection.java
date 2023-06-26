package com.fimi.app.x8s.adapter.section;

import com.fimi.x8sdk.entity.X8FlightLogFile;

import java.util.List;


public class X8FlightLogSection extends AbsSection {
    private List<X8FlightLogFile> list;

    public X8FlightLogSection(List<X8FlightLogFile> list, boolean hasHeader) {
        super(hasHeader);
        this.list = list;
    }

    public List<X8FlightLogFile> getList() {
        return this.list;
    }

    public void setList(List<X8FlightLogFile> list) {
        this.list = list;
    }

    @Override
    public int getContentItemsTotal() {
        return this.list.size();
    }
}

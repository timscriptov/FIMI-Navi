package com.fimi.app.x8s.adapter.section;


public abstract class AbsSection {
    private boolean hasHeader;

    public AbsSection(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public abstract int getContentItemsTotal();

    public boolean isHasHeader() {
        return this.hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public final int getSectionItemsTotal() {
        int contentItemsTotal = getContentItemsTotal();
        return (this.hasHeader ? 1 : 0) + contentItemsTotal;
    }
}

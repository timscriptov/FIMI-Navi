package com.fimi.app.x8s.map.interfaces;

/* loaded from: classes.dex */
public abstract class AbsAiSurroundManager extends AbsBaseManager {
    public abstract void addEllipse(double d, double d2, float f, float f2);

    public abstract void addPolylinescircle(boolean z, double d, double d2, double d3, double d4, int i, int i2);

    public abstract void clearSurroundMarker();

    public abstract void drawAiSurroundCircle(double d, double d2, double d3);

    public abstract float getSurroundRadius(double d, double d2, double d3, double d4);

    public abstract void reSetAiSurroundCircle(double d, double d2, float f);

    public abstract void setAiSurroundCircle(double d, double d2, float f);

    public abstract void setAiSurroundMark(double d, double d2);

    @Override // com.fimi.app.x8s.map.interfaces.AbsBaseManager
    public void setMarkerViewInfo(float height) {
    }
}

package com.fimi.x8sdk.ivew;


public interface IX8FlightLogListAction {
    void noDataHint();

    void startSyn();

    void synCompleteRefreshUI(boolean z);

    void synTotalProgress(int i);
}

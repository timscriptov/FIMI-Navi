package com.fimi.kernel.dataparser;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;


public abstract class BaseIoFilterAdapter extends IoFilterAdapter {
    @Override
    public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
        super.messageReceived(nextFilter, session, message);
    }
}

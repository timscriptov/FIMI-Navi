package com.fimi.x8sdk.connect.datatype;

import com.fimi.kernel.connect.interfaces.IRetransmissionUsbHandle;
import com.fimi.kernel.connect.session.NoticeManager;


public class MediaDataChanel implements IDataChanel {
    private IRetransmissionUsbHandle retransmissionUsbHandle;

    @Override
    public void forwardData(byte[] data) {
        NoticeManager.getInstance().onMediaDataCallBack(data);
        int cmdKey = data[0];
        if (cmdKey == 0) {
            if (this.retransmissionUsbHandle != null) {
                this.retransmissionUsbHandle.removeFromListByUsbCmdKey(cmdKey);
            }
        } else if (cmdKey == 1) {
            int offSet = getInt(7, data);
            if (this.retransmissionUsbHandle != null) {
                this.retransmissionUsbHandle.removeFormListByOffset(offSet);
            }
        }
    }

    public void setRetransmissionUsbHandle(IRetransmissionUsbHandle retransmissionUsbHandle) {
        this.retransmissionUsbHandle = retransmissionUsbHandle;
    }

    public int getInt(int index, byte[] data) {
        int result = 0 | ((data[index + 3] & 255) << 24);
        return result | ((data[index + 2] & 255) << 16) | ((data[index + 1] & 255) << 8) | (data[index] & 255);
    }

    public short getShort(int index, byte[] data) {
        short result = (short) (((data[index + 1] & 255) << 8) | 0);
        return (short) ((data[index] & 255) | result);
    }
}

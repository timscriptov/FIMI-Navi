package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.BaseRespJson;


public class AckCamJsonInfo extends BaseRespJson {
    public String toString() {
        return "AckCamJsonInfo{rval=" + this.rval + ", msg_id=" + this.msg_id + ", type='" + this.type + "'" + ", param=" + this.param + '}';
    }
}

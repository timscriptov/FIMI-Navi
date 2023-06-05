package com.fimi.x8sdk.command;


public abstract class X8BaseCmd {
    public static final byte MODULE_BATTERY = 15;
    public static final byte MODULE_CAMERA = 3;
    public static final byte MODULE_CV = 10;
    public static final byte MODULE_ESC = 18;
    public static final byte MODULE_FC = 2;
    public static final byte MODULE_GCS = 7;
    public static final byte MODULE_GIMBAL = 8;
    public static final byte MODULE_NFZ = 17;
    public static final byte MODULE_RC = 13;
    public static final byte MODULE_REPEATER_VEHICLE = 14;
    public static final byte MODULE_REPEAT_RC = 16;
    public static final byte MODULE_SERVO = 19;
    public static final byte MODULE_UAV = 1;
    protected static final byte ENCRYPRTION_YES = 1;
    protected static final byte ENCRYPTION_NO = 0;
    protected static final byte FM_LINK_VERSION = 4;
    protected static final byte TYPE_ACK = 2;
    protected static final byte TYPE_AUTOSEND = 0;
    protected static final byte TYPE_CMD = 1;
    static int count = 0;
    protected short seqIndex;

    public X8BaseCmd() {
        this.seqIndex = (short) 0;
        int i = count;
        count = i + 1;
        this.seqIndex = (short) i;
        if (count == 32766) {
            count = 0;
        }
    }


    public enum X8S_Module {
        MODULE_IDLE,
        MODULE_UAV,
        MODULE_FC,
        MODULE_CAMERA,
        MODULE_OPTFLOW,
        MODULE_OBSAVOID,
        MODULE_HTTP,
        MODULE_GCS,
        MODULE_GIMBAL,
        MODULE_BLACKBOX,
        MODULE_CV,
        MODULE_SV_DWN,
        MODULE_SV_FW,
        MODULE_RC,
        MODULE_REPEATER_VEHICLE,
        MODULE_BATTERY,
        MODULE_REPEATER_RC,
        MODULE_NFZ,
        MODULE_ESC,
        MODULE_SERVO,
        MODULE_Default0X14,
        MODULE_Default0X15,
        MODULE_ULTRASONIC
    }
}

package com.fimi.x8sdk.rtp;

import android.content.Context;

import com.fimi.android.app.R;

/* loaded from: classes2.dex */
public class X8Rtp {
    public static boolean isRtpAllShow = false;
    public static boolean simulationTest = false;

    public static String getFcNavString(Context context, int code) {
        String s = isRtpAllShow ? context.getString(R.string.cmd_fail) + "error code=" + code : "";
        switch (code) {
            case 1:
                String s2 = context.getString(R.string.x8_nav_rtp1);
                return s2;
            case 2:
                String s3 = context.getString(R.string.x8_nav_rtp2);
                return s3;
            case 3:
                String s4 = context.getString(R.string.x8_nav_rtp3);
                return s4;
            case 4:
                String s5 = context.getString(R.string.x8_nav_rtp4);
                return s5;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 20:
            case 21:
            case 24:
            case 27:
            case 30:
            case 32:
            case 34:
            case 35:
            case 36:
            case 39:
            case 40:
            case 41:
            case 48:
            case 49:
            case 50:
            case 51:
            default:
                return s;
            case 12:
                String s6 = context.getString(R.string.x8_nav_rtp12);
                return s6;
            case 13:
                String s7 = context.getString(R.string.x8_nav_rtp13);
                return s7;
            case 14:
                String s8 = context.getString(R.string.x8_nav_rtp14);
                return s8;
            case 15:
                String s9 = context.getString(R.string.x8_nav_rtp15);
                return s9;
            case 16:
                String s10 = context.getString(R.string.x8_nav_rtp16);
                return s10;
            case 17:
                String s11 = context.getString(R.string.x8_nav_rtp17);
                return s11;
            case 18:
                String s12 = context.getString(R.string.x8_nav_rtp18);
                return s12;
            case 19:
                String s13 = context.getString(R.string.x8_nav_rtp19);
                return s13;
            case 22:
                String s14 = context.getString(R.string.x8_nav_rtp22);
                return s14;
            case 23:
                String s15 = context.getString(R.string.x8_nav_rtp23);
                return s15;
            case 25:
                String s16 = context.getString(R.string.x8_nav_rtp25);
                return s16;
            case 26:
                String s17 = context.getString(R.string.x8_nav_rtp26);
                return s17;
            case 28:
                String s18 = context.getString(R.string.x8_nav_rtp28);
                return s18;
            case 29:
                String s19 = context.getString(R.string.x8_nav_rtp29);
                return s19;
            case 31:
                String s20 = context.getString(R.string.x8_nav_rtp31);
                return s20;
            case 33:
                String s21 = context.getString(R.string.x8_nav_rtp33);
                return s21;
            case 37:
                String s22 = context.getString(R.string.x8_nav_rtp64);
                return s22;
            case 38:
                String s23 = context.getString(R.string.x8_nav_rtp38);
                return s23;
            case 42:
                String s24 = context.getString(R.string.x8_nav_rtp42);
                return s24;
            case 43:
                String s25 = context.getString(R.string.x8_nav_rtp43);
                return s25;
            case 44:
                String s26 = context.getString(R.string.x8_nav_rtp44);
                return s26;
            case 45:
                String s27 = context.getString(R.string.x8_nav_rtp45);
                return s27;
            case 46:
                String s28 = context.getString(R.string.x8_nav_rtp46);
                return s28;
            case 47:
                String s29 = context.getString(R.string.x8_nav_rtp47);
                return s29;
            case 52:
                String s30 = context.getString(R.string.x8_nav_rtp52);
                return s30;
            case 53:
                String s31 = context.getString(R.string.x8_nav_rtp53);
                return s31;
            case 54:
                String s32 = context.getString(R.string.x8_nav_rtp54);
                return s32;
            case 55:
                String s33 = context.getString(R.string.x8_nav_rtp55);
                return s33;
            case 56:
                String s34 = context.getString(R.string.x8_nav_rtp56);
                return s34;
            case 57:
                String s35 = context.getString(R.string.x8_nav_rtp57);
                return s35;
            case 58:
                String s36 = context.getString(R.string.x8_nav_rtp58);
                return s36;
            case 59:
                String s37 = context.getString(R.string.x8_nav_rtp59);
                return s37;
            case 60:
                String s38 = context.getString(R.string.x8_nav_rtp60);
                return s38;
            case 61:
                String s39 = context.getString(R.string.x8_nav_rtp61);
                return s39;
            case 62:
                String s40 = context.getString(R.string.x8_nav_rtp62);
                return s40;
            case 63:
                String s41 = context.getString(R.string.x8_nav_rtp63);
                return s41;
            case 64:
                String s42 = context.getString(R.string.x8_nav_rtp64);
                return s42;
        }
    }

    public static String getRtpStringFcCtrl(Context context, int code) {
        String s = isRtpAllShow ? context.getString(R.string.cmd_fail) + "error code=" + code : "";
        switch (code) {
            case 1:
                String s2 = context.getString(R.string.x8_ctrl_rtp1);
                return s2;
            case 2:
                String s3 = context.getString(R.string.x8_ctrl_rtp2);
                return s3;
            case 80:
                String s4 = context.getString(R.string.x8_ctrl_rtp50);
                return s4;
            case 81:
                String s5 = context.getString(R.string.x8_ctrl_rtp51);
                return s5;
            case 96:
                String s6 = context.getString(R.string.x8_ctrl_rtp60);
                return s6;
            case 97:
                String s7 = context.getString(R.string.x8_ctrl_rtp61);
                return s7;
            case 98:
                String s8 = context.getString(R.string.x8_ctrl_rtp62);
                return s8;
            case 113:
                String s9 = context.getString(R.string.x8_ctrl_rtp71);
                return s9;
            case 114:
                String s10 = context.getString(R.string.x8_ctrl_rtp72);
                return s10;
            case 116:
                String s11 = context.getString(R.string.x8_ctrl_rtp74);
                return s11;
            case 117:
                String s12 = context.getString(R.string.x8_ctrl_rtp75);
                return s12;
            case 118:
                String s13 = context.getString(R.string.x8_ctrl_rtp76);
                return s13;
            case 119:
                String s14 = context.getString(R.string.x8_ctrl_rtp77);
                return s14;
            case 121:
                String s15 = context.getString(R.string.x8_ctrl_rtp79);
                return s15;
            case 122:
                String s16 = context.getString(R.string.x8_ctrl_rtp7A);
                return s16;
            case 123:
                String s17 = context.getString(R.string.x8_ctrl_rtp7B);
                return s17;
            case 124:
                String s18 = context.getString(R.string.x8_ctrl_rtp7C);
                return s18;
            case 126:
                String s19 = context.getString(R.string.x8_ctrl_rtp7E);
                return s19;
            default:
                return s;
        }
    }

    public static String getRtpStringCamera(Context context, int code) {
        switch (code) {
            case 1:
                String s = context.getString(R.string.x8_camera_rtp1);
                return s;
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                return "";
            case 3:
                String s2 = context.getString(R.string.x8_camera_rtp3);
                return s2;
            case 8:
                String s3 = context.getString(R.string.x8_camera_rtp8);
                return s3;
            case 9:
                String s4 = context.getString(R.string.x8_camera_rtp9);
                return s4;
            case 10:
                String s5 = context.getString(R.string.x8_camera_rtp10);
                return s5;
        }
    }
}

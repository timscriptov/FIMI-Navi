package com.fimi.app.x8s.controls.fcsettting.flightlog;

import android.os.Handler;

import com.fimi.app.x8s.adapter.X8FlightLogAdapter;
import com.fimi.app.x8s.adapter.section.X8FlightLogSection;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.kernel.utils.TimerUtil;
import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.entity.X8FlightLogFile;
import com.fimi.x8sdk.util.X8FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class X8FlightLogFileSeachDeleteThread extends Thread {
    private final boolean deleteAll;
    private final Handler handler;
    private final boolean isSeach;
    private final boolean uploadSuccessfulAll;
    private final X8FlightLogAdapter x8FlightLogAdapter;
    private int flightDurationNumber;
    private float flightMileageNumber;

    public X8FlightLogFileSeachDeleteThread(X8FlightLogAdapter x8FlightLogAdapter, Handler handler, boolean seachOrDelete, boolean deleteAll, boolean uploadSuccessfulAll) {
        this.x8FlightLogAdapter = x8FlightLogAdapter;
        this.handler = handler;
        this.isSeach = seachOrDelete;
        this.deleteAll = deleteAll;
        this.uploadSuccessfulAll = uploadSuccessfulAll;
    }

    @Override
    public void run() {
        super.run();
        if (this.isSeach) {
            seachFile();
        } else {
            deleteFile();
        }
    }

    private void deleteFile() {
        List<X8FlightLogFile> x8FlightLogFiles;
        if (this.x8FlightLogAdapter != null && (x8FlightLogFiles = this.x8FlightLogAdapter.getSection().getList()) != null && x8FlightLogFiles.size() > 0) {
            X8FileHelper.deleteFlightLogFiles(x8FlightLogFiles, this.deleteAll);
            this.handler.sendEmptyMessage(2);
            seachFile();
        }
    }

    public void seachFile() {
        FileInputStream fileInputStream = null;
        List<File> list = X8FileHelper.flightLogListDirs("");
        if (list != null && list.size() > 0) {
            Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            List<X8FlightLogFile> lists = new ArrayList<>();
            int total = 0;
            int complete = 0;
            for (File file : list) {
                List<File> list2 = X8FileHelper.flightLogListDirs(file.getAbsolutePath());
                if (list2 != null && list2.size() > 0) {
                    for (File file2 : list2) {
                        X8FlightLogFile x8FlightLogFile = new X8FlightLogFile();
                        x8FlightLogFile.setFile(file);
                        x8FlightLogFile.setFileName(file.getName());
                        Matcher matcher = p.matcher(file2.getName());
                        if (!file2.getName().equals(X8FcLogManager.getInstance().getCurrentWriteFile())) {
                            if (matcher.find()) {
                                matcher.group(0);
                                boolean ret = x8FlightLogFile.setPlaybackFile(file2, this.uploadSuccessfulAll);
                                if (ret) {
                                    complete++;
                                }
                                lists.add(x8FlightLogFile);
                                total++;
                            }
                            try {
                                try {
                                    FileInputStream fileInputStream2 = new FileInputStream(file2);
                                    try {
                                        long fileSize = fileInputStream2.getChannel().size();
                                        int offset = (int) (fileSize - 12);
                                        fileInputStream2.skip(offset);
                                        if (fileSize != 0) {
                                            byte[] bytes = new byte[12];
                                            fileInputStream2.read(bytes, 0, bytes.length);
                                            if (bytes[0] == -3 && bytes[1] == 1) {
                                                int flightDuration = ByteUtil.bytesToInt(bytes, 4);
                                                x8FlightLogFile.setFlightDuration(flightDuration);
                                                this.flightDurationNumber += flightDuration;
                                                float flightMileage = ByteUtil.byte2float(bytes, 8);
                                                if (flightMileage < 1000.0f) {
                                                    x8FlightLogFile.setFlightMileage(Float.valueOf(Math.round(flightMileage)).intValue() + "");
                                                } else {
                                                    x8FlightLogFile.setFlightMileage(X8NumberUtil.getDistanceNumberNoPrexString(flightMileage, 1));
                                                }
                                                if (flightMileage > 0.0f) {
                                                    this.flightMileageNumber += flightMileage;
                                                }
                                            }
                                        }
                                        if (fileInputStream2 != null) {
                                            try {
                                                fileInputStream2.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                fileInputStream = fileInputStream2;
                                            }
                                        }
                                        fileInputStream = fileInputStream2;
                                    } catch (Exception e2) {
                                        fileInputStream = fileInputStream2;
                                        if (fileInputStream != null) {
                                            try {
                                                fileInputStream.close();
                                            } catch (IOException e3) {
                                                e3.printStackTrace();
                                            }
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        fileInputStream = fileInputStream2;
                                        if (fileInputStream != null) {
                                            try {
                                                fileInputStream.close();
                                            } catch (IOException e4) {
                                                e4.printStackTrace();
                                            }
                                        }
                                        throw th;
                                    }
                                } catch (Exception e5) {
                                }
                            } catch (Throwable th2) {
                            }
                        }
                    }
                }
            }
            if (lists.size() > 0) {
                sort(lists);
                this.x8FlightLogAdapter.addSection("", new X8FlightLogSection(lists, true));
                if (this.flightMileageNumber >= 1000.0f) {
                    this.x8FlightLogAdapter.setPlaybackDistance(X8NumberUtil.getDistanceNumberNoPrexString(this.flightMileageNumber / 1000.0f, 1) + "km");
                } else {
                    this.x8FlightLogAdapter.setPlaybackDistance(Float.valueOf(this.flightMileageNumber).intValue() + "m");
                }
                String totalTime = TimerUtil.getInstance().stringForTime(this.flightDurationNumber);
                this.x8FlightLogAdapter.setPlaybackTotalTime(totalTime);
                this.handler.obtainMessage(0, complete, total).sendToTarget();
                return;
            }
            this.handler.sendEmptyMessage(1);
            return;
        }
        this.handler.sendEmptyMessage(1);
    }

    public void sort(List<X8FlightLogFile> list) {
        Collections.sort(list, new Comparator<X8FlightLogFile>() {
            @Override
            public int compare(X8FlightLogFile arg0, X8FlightLogFile arg1) {
                int a = Integer.parseInt(arg1.getFileLogCollectState()) - Integer.parseInt(arg0.getFileLogCollectState());
                if (a != 0) {
                    return a < 0 ? 3 : -1;
                }
                int mark = arg1.getPlaybackFile().getName().compareTo(arg0.getPlaybackFile().getName());
                return mark;
            }
        });
    }

    public void sortGroup(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String arg0, String arg1) {
                int mark = arg1.compareTo(arg0);
                return mark;
            }
        });
    }
}

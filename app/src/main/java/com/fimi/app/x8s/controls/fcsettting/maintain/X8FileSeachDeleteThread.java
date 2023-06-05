package com.fimi.app.x8s.controls.fcsettting.maintain;

import android.os.Handler;

import com.fimi.app.x8s.adapter.X8B2oxAdapter;
import com.fimi.app.x8s.adapter.section.X8B2oxSection;
import com.fimi.app.x8s.entity.X8B2oxFile;
import com.fimi.x8sdk.util.X8FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class X8FileSeachDeleteThread extends Thread {
    private Handler handler;
    private boolean isSeach;
    private X8B2oxAdapter mX8B2oxAdapter;

    public X8FileSeachDeleteThread(X8B2oxAdapter mX8B2oxAdapter, Handler handler, boolean seachOrDelete) {
        this.mX8B2oxAdapter = mX8B2oxAdapter;
        this.handler = handler;
        this.isSeach = seachOrDelete;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        if (this.isSeach) {
            seachFile();
        } else {
            deleteFile();
        }
    }

    private void deleteFile() {
        X8FileHelper.deleteFiles();
        this.handler.sendEmptyMessage(2);
        seachFile();
    }

    public void seachFile() {
        List<File> list = X8FileHelper.listDirs();
        sortFileOfDate(list);
        if (list != null && list.size() > 0) {
            Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            List<String> groupList = new ArrayList<>();
            List<List<X8B2oxFile>> lists = new ArrayList<>();
            int total = 0;
            int complete = 0;
            for (File file : list) {
                X8B2oxFile b2oxFile = new X8B2oxFile();
                Matcher matcher = p.matcher(file.getName());
                if (matcher.find()) {
                    String date = matcher.group(0);
                    if (!groupList.contains(date)) {
                        groupList.add(date);
                        lists.add(new ArrayList<>());
                    }
                    boolean ret = b2oxFile.setFile(file, date);
                    if (ret) {
                        complete++;
                    }
                    lists.get(groupList.size() - 1).add(b2oxFile);
                    total++;
                }
            }
            if (groupList.size() > 0) {
                int i = 0;
                for (String title : groupList) {
                    this.mX8B2oxAdapter.addSection(title, new X8B2oxSection(title, lists.get(i), true));
                    i++;
                }
                this.handler.obtainMessage(0, complete, total).sendToTarget();
                return;
            }
            this.handler.sendEmptyMessage(1);
            return;
        }
        this.handler.sendEmptyMessage(1);
    }

    public void sort(List<X8B2oxFile> list) {
        Collections.sort(list, new Comparator<X8B2oxFile>() { // from class: com.fimi.app.x8s.controls.fcsettting.maintain.X8FileSeachDeleteThread.1
            @Override // java.util.Comparator
            public int compare(X8B2oxFile arg0, X8B2oxFile arg1) {
                int mark = arg1.getFile().getName().compareTo(arg0.getFile().getName());
                return mark;
            }
        });
    }

    public void sortGroup(List<String> list) {
        Collections.sort(list, new Comparator<String>() { // from class: com.fimi.app.x8s.controls.fcsettting.maintain.X8FileSeachDeleteThread.2
            @Override // java.util.Comparator
            public int compare(String arg0, String arg1) {
                int mark = arg1.compareTo(arg0);
                return mark;
            }
        });
    }

    private void sortFileOfDate(List fileList) {
        if (fileList != null && fileList.size() > 0) {
            Collections.sort(fileList, new Comparator<File>() { // from class: com.fimi.app.x8s.controls.fcsettting.maintain.X8FileSeachDeleteThread.3
                @Override // java.util.Comparator
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0) {
                        return -1;
                    }
                    if (diff == 0) {
                        return 0;
                    }
                    return 1;
                }

                @Override // java.util.Comparator
                public boolean equals(Object obj) {
                    return true;
                }
            });
        }
    }
}

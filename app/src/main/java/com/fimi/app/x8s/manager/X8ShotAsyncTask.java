package com.fimi.app.x8s.manager;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.fimi.app.x8s.interfaces.IFimiFpvShot;
import com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady;
import com.fimi.app.x8s.interfaces.IFimiShotResult;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;

/* loaded from: classes.dex */
public class X8ShotAsyncTask extends AsyncTask<String, String, Bitmap> {
    private X8sMainActivity activity;
    private Bitmap btp = null;
    private IFimiShotResult callback;
    private int type;

    public X8ShotAsyncTask(X8sMainActivity activity, IFimiShotResult callback, int type) {
        this.activity = activity;
        this.callback = callback;
        this.type = type;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override // android.os.AsyncTask
    public Bitmap doInBackground(String... params) {
        if (this.type == 0) {
            this.activity.getmMapVideoController().snapshot(new IFimiOnSnapshotReady() { // from class: com.fimi.app.x8s.manager.X8ShotAsyncTask.1
                @Override // com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady
                public void onSnapshotReady(Bitmap btp) {
                    X8ShotAsyncTask.this.btp = btp;
                }
            });
        } else {
            this.activity.getmMapVideoController().fpvShot(new IFimiFpvShot() { // from class: com.fimi.app.x8s.manager.X8ShotAsyncTask.2
                @Override // com.fimi.app.x8s.interfaces.IFimiFpvShot
                public void onFpvshotReady(Bitmap btp) {
                    X8ShotAsyncTask.this.btp = btp;
                }
            });
        }
        for (int i = 0; i < 75 && this.btp == null; i++) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.btp;
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        this.callback.onShotResult(bitmap);
    }

    public void recycleBitmap() {
        if (this.btp != null && !this.btp.isRecycled()) {
            this.btp.recycle();
        }
    }
}

package com.fimi.app.x8s.manager;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.fimi.app.x8s.interfaces.IFimiShotResult;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;


public class X8ShotAsyncTask extends AsyncTask<String, String, Bitmap> {
    private final X8sMainActivity activity;
    private final IFimiShotResult callback;
    private final int type;
    private Bitmap btp = null;

    public X8ShotAsyncTask(X8sMainActivity activity, IFimiShotResult callback, int type) {
        this.activity = activity;
        this.callback = callback;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public Bitmap doInBackground(String... params) {
        if (this.type == 0) {
            this.activity.getmMapVideoController().snapshot(btp -> X8ShotAsyncTask.this.btp = btp);
        } else {
            this.activity.getmMapVideoController().fpvShot(btp -> X8ShotAsyncTask.this.btp = btp);
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

    @Override
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

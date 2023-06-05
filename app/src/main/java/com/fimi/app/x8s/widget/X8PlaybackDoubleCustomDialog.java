package com.fimi.app.x8s.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.widget.impl.NoDoubleClickListener;

public class X8PlaybackDoubleCustomDialog extends X8sBaseDialog {
    private final X8TabHost x8TabhostPlaybackSyn;
    private int x8PlaybackSynType;

    public X8PlaybackDoubleCustomDialog(@NonNull Context context, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_playback_double_dialog_custom);
        this.x8TabhostPlaybackSyn = findViewById(R.id.x8_tabhost_playback_syn);
        TextView tvLeft = findViewById(R.id.btn_left);
        TextView tvRight = findViewById(R.id.btn_right);
        tvLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeft();
            }
            X8PlaybackDoubleCustomDialog.this.dismiss();
        });
        tvRight.setOnClickListener(new NoDoubleClickListener(500) {
            @Override
            protected void onNoDoubleClick(View v) {
                if (listener != null) {
                    X8PlaybackDoubleCustomDialog.this.x8PlaybackSynType = X8PlaybackDoubleCustomDialog.this.x8TabhostPlaybackSyn.getSelectIndex();
                    listener.onRight();
                }
                X8PlaybackDoubleCustomDialog.this.dismiss();
            }
        });
    }

    public int getX8PlaybackSynType() {
        return this.x8PlaybackSynType;
    }

    public interface onDialogButtonClickListener {
        void onLeft();

        void onRight();
    }
}

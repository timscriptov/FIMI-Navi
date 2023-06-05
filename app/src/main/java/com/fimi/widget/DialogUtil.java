package com.fimi.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.AbViewUtil;

public class DialogUtil extends Dialog {
    public DialogUtil(Context context) {
        super(context);
    }

    protected DialogUtil(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public DialogUtil(Context context, int theme) {
        super(context, theme);
    }

    public static class DoubleButtonBuilder {
        private final DialogUtil dialog;
        public Context mContext;
        private String content;
        private boolean downLoadFirmware;
        private int ensureColor;
        private DialogInterface.OnClickListener leftButtonClickListener;
        private String leftButtonText;
        private String message;
        private String rightButtonText;
        private DialogInterface.OnClickListener righteButtonClickListener;
        private DialogInterface.OnClickListener singleButtonClickListern;
        private String singleButtonText;
        private Drawable singleDrawable;
        private String title;
        private int rightTextButtonColor = -1;
        private int leftTextButtonColor = -1;

        public DoubleButtonBuilder(Context context) {
            this.mContext = context;
            this.dialog = new DialogUtil(this.mContext, R.style.DropDialog1);
        }

        public DoubleButtonBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public DoubleButtonBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public DoubleButtonBuilder setRightButtonText(String rightButtonText) {
            this.rightButtonText = rightButtonText;
            return this;
        }

        public DoubleButtonBuilder setLeftButtonText(String leftButtonText) {
            this.leftButtonText = leftButtonText;
            return this;
        }

        public DoubleButtonBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public DoubleButtonBuilder setRightTextButtonColor(int rightTextButtonColor) {
            this.rightTextButtonColor = rightTextButtonColor;
            return this;
        }

        public DoubleButtonBuilder setLeftTextButtonColor(int leftTextButtonColor) {
            this.leftTextButtonColor = leftTextButtonColor;
            return this;
        }

        public DoubleButtonBuilder setRightButtonText(String rightButtonText, DialogInterface.OnClickListener listern) {
            this.rightButtonText = rightButtonText;
            this.righteButtonClickListener = listern;
            return this;
        }

        public DoubleButtonBuilder setLeftButtonText(String leftButtonText, DialogInterface.OnClickListener listener) {
            this.leftButtonText = leftButtonText;
            this.leftButtonClickListener = listener;
            return this;
        }

        public DoubleButtonBuilder setSingleButtonText(String singleButtonText, DialogInterface.OnClickListener listener) {
            this.singleButtonText = singleButtonText;
            this.singleButtonClickListern = listener;
            return this;
        }

        public DoubleButtonBuilder setSingleDrawable(Drawable singleDrawable) {
            this.singleDrawable = singleDrawable;
            return this;
        }

        public DoubleButtonBuilder setSingleButtonText(String singleButtonText) {
            this.singleButtonText = singleButtonText;
            return this;
        }

        public DoubleButtonBuilder setEnsureColor(int color) {
            this.ensureColor = color;
            return this;
        }

        public DoubleButtonBuilder setDownLoadFirmware(boolean downLoadFirmware) {
            this.downLoadFirmware = downLoadFirmware;
            return this;
        }

        public DialogUtil create() {
            View layout;
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (this.downLoadFirmware) {
                layout = inflater.inflate(R.layout.down_firmware_dialog_content_double_button, null);
            } else if (this.content != null) {
                layout = inflater.inflate(R.layout.vertical_screen_dialog_content_double_button, null);
            } else if (this.singleDrawable != null) {
                layout = inflater.inflate(R.layout.vertical_screen_single_dialog_double_button, null);
            } else {
                layout = inflater.inflate(R.layout.x9_screen_dialog_double_button, null);
            }
            Button leftBtn = layout.findViewById(R.id.left_btn);
            Button rightBtn = layout.findViewById(R.id.right_btn);
            TextView msgTv = layout.findViewById(R.id.msg_tv);
            Button singleBtn = layout.findViewById(R.id.single_btn);
            if (this.rightButtonText != null) {
                rightBtn.setText(this.rightButtonText);
            }
            if (this.leftButtonText != null) {
                leftBtn.setText(this.leftButtonText);
            }
            if (this.message != null) {
                msgTv.setText(this.message);
            }
            if (this.rightTextButtonColor != -1) {
                rightBtn.setTextColor(this.rightTextButtonColor);
            }
            if (this.leftTextButtonColor != -1) {
                leftBtn.setTextColor(this.leftTextButtonColor);
            }
            if (this.righteButtonClickListener != null) {
                rightBtn.setOnClickListener(v -> {
                    dialog.dismiss();
                    righteButtonClickListener.onClick(dialog, -1);
                });
            }
            if (this.leftButtonClickListener != null) {
                leftBtn.setOnClickListener(v -> {
                    dialog.dismiss();
                    leftButtonClickListener.onClick(dialog, -2);
                });
            }
            if (this.singleButtonText != null) {
                singleBtn.setText(this.singleButtonText);
            }
            if (this.singleButtonClickListern != null) {
                singleBtn.setOnClickListener(v -> {
                    dialog.dismiss();
                    singleButtonClickListern.onClick(dialog, -1);
                });
            }
            if (this.content != null) {
                TextView contentTv = layout.findViewById(R.id.content_tv);
                contentTv.setText(this.content);
            }
            if (this.singleDrawable != null) {
                ImageView contentIv = layout.findViewById(R.id.content_iv);
                contentIv.setBackground(this.singleDrawable);
            }
            this.dialog.setContentView(layout);
            this.dialog.getWindow().setGravity(17);
            Window window = this.dialog.getWindow();
            window.setLayout((int) this.mContext.getResources().getDimension(R.dimen.dialog_width), -2);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Window dialogWindow = this.dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.alpha = 0.9f;
            window.setAttributes(lp);
            dialogWindow.setAttributes(lp);
            View decorView = this.dialog.getWindow().getDecorView();
            decorView.setBackgroundColor(0);
            return this.dialog;
        }

        public DialogUtil getDialog() {
            return this.dialog;
        }
    }

    public static class ProgressDialogBuilder {
        private final DialogUtil dialog;
        private final Context mContext;
        boolean isShowImage = false;
        private String message;

        public ProgressDialogBuilder(Context context) {
            this.mContext = context;
            this.dialog = new DialogUtil(this.mContext, R.style.DropDialog1);
        }

        public ProgressDialogBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ProgressDialogBuilder setMessage(String message, boolean isShowImage) {
            this.message = message;
            this.isShowImage = isShowImage;
            return this;
        }

        public DialogUtil create() {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.x9_screen_dialog_double_button, null);
            TextView textView = layout.findViewById(R.id.progress_tv);
            ImageView imageView = layout.findViewById(R.id.img_show);
            ProgressBar progressBar = layout.findViewById(R.id.progressBar);
            if (this.isShowImage) {
                imageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
            if (this.message != null) {
                textView.setText(this.message);
            }
            this.dialog.setContentView(layout);
            this.dialog.getWindow().setGravity(17);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Window window = this.dialog.getWindow();
            window.setLayout((int) this.mContext.getResources().getDimension(R.dimen.dialog_width), -2);
            Window dialogWindow = this.dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.alpha = 0.9f;
            window.setAttributes(lp);
            float screenheight = Math.max(AbViewUtil.getScreenHeight(this.mContext), AbViewUtil.getScreenWidth(this.mContext));
            lp.height = (int) ((218.0f * screenheight) / 1920.0f);
            dialogWindow.setAttributes(lp);
            View decorView = this.dialog.getWindow().getDecorView();
            decorView.setBackgroundColor(0);
            return this.dialog;
        }

        public DialogUtil getDialog() {
            return this.dialog;
        }
    }
}

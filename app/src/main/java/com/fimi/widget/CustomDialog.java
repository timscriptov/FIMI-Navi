package com.fimi.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.AbViewUtil;
import com.fimi.kernel.utils.FontUtil;

public class CustomDialog extends Dialog {
    private static DialogManager.OnDismissListener onDismissListener;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public static class Builder {
        private final Context context;
        private View contentView;
        private DialogInterface.OnClickListener leftButtonClickListener;
        private String leftButtonText;
        private CharSequence message;
        private DialogInterface.OnClickListener rightButtonClickListener;
        private String rightButtonText;
        private DialogInterface.OnClickListener singleButtonClickListener;
        private String singleButtonText;
        private String title;
        private int rightButtonColor = -1;
        private int leftButtonColor = -1;
        private int singleButtonColor = -1;
        private int titleColor = -1;
        private boolean isSingle = false;
        private boolean clickOutIsCancle = false;
        private boolean isShowVirtKey = true;
        private boolean isVerticalScreen = false;
        private int gravity = -1;
        private boolean isSpan = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = this.context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) this.context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setRightButton(int rightButtonText, DialogInterface.OnClickListener listener) {
            this.rightButtonText = (String) this.context.getText(rightButtonText);
            this.rightButtonClickListener = listener;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setRightButton(String rightButtonText, DialogInterface.OnClickListener listener) {
            this.rightButtonText = rightButtonText;
            this.rightButtonClickListener = listener;
            return this;
        }

        public Builder setDissmssListener(DialogManager.OnDismissListener dissmssListener) {
            DialogManager.OnDismissListener unused = CustomDialog.onDismissListener = dissmssListener;
            return this;
        }

        public Builder setLeftButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            this.leftButtonText = (String) this.context.getText(negativeButtonText);
            this.leftButtonClickListener = listener;
            return this;
        }

        public Builder setLeftButton(String leftButtonText, DialogInterface.OnClickListener listener) {
            this.leftButtonText = leftButtonText;
            this.leftButtonClickListener = listener;
            return this;
        }

        public Builder setRightButtonColor(int rightButtonColor) {
            this.rightButtonColor = rightButtonColor;
            return this;
        }

        public Builder setLeftButtonColor(int leftButtonColor) {
            this.leftButtonColor = leftButtonColor;
            return this;
        }

        public Builder setSingleButtonColor(int singleButtonColor) {
            this.singleButtonColor = singleButtonColor;
            return this;
        }

        public Builder setSingle(boolean single) {
            this.isSingle = single;
            return this;
        }

        public Builder setSingleButton(int singleButtonText, DialogInterface.OnClickListener listener) {
            this.singleButtonText = (String) this.context.getText(singleButtonText);
            this.singleButtonClickListener = listener;
            return this;
        }

        public Builder setSingleButton(String singleButtonText, DialogInterface.OnClickListener listener) {
            this.singleButtonText = singleButtonText;
            this.singleButtonClickListener = listener;
            return this;
        }

        public Builder setClickOutIsCancle(boolean clickOutIsCancle) {
            this.clickOutIsCancle = clickOutIsCancle;
            return this;
        }

        public Builder setShowVirtKey(boolean isShowVirtKey) {
            this.isShowVirtKey = isShowVirtKey;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setVerticalScreen(boolean verticalScreen) {
            this.isVerticalScreen = verticalScreen;
            return this;
        }

        public Builder setSpan(boolean span) {
            this.isSpan = span;
            return this;
        }

        public CustomDialog create() {
            View layout;
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(this.context, R.style.fimisdk_custom_dialog);
            if (this.isVerticalScreen) {
                layout = inflater.inflate(R.layout.fimisdk_vertical_dialog_custom, null);
            } else {
                layout = inflater.inflate(R.layout.fimisdk_dialog_custom, null);
            }
            dialog.addContentView(layout, new LinearLayout.LayoutParams(-1, -2));
            if (this.rightButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_right)).setText(this.rightButtonText);
                if (this.rightButtonClickListener != null) {
                    layout.findViewById(R.id.btn_right).setOnClickListener(v -> Builder.this.rightButtonClickListener.onClick(dialog, -1));
                }
            }
            if (this.leftButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_left)).setText(this.leftButtonText);
                if (this.leftButtonClickListener != null) {
                    layout.findViewById(R.id.btn_left).setOnClickListener(v -> Builder.this.leftButtonClickListener.onClick(dialog, -2));
                }
            }
            if (this.singleButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_single)).setText(this.singleButtonText);
                if (this.singleButtonClickListener != null) {
                    layout.findViewById(R.id.btn_single).setOnClickListener(v -> Builder.this.singleButtonClickListener.onClick(dialog, -2));
                }
            }
            if (this.message != null) {
                ((TextView) layout.findViewById(R.id.tv_content)).setText(this.message);
                ((TextView) layout.findViewById(R.id.tv_content_sigle)).setText(this.message);
            }
            if (this.titleColor != -1) {
                ((TextView) layout.findViewById(R.id.tv_title)).setTextColor(this.titleColor);
            }
            if (this.title != null && !this.title.equals("")) {
                TextView textView = layout.findViewById(R.id.tv_title);
                TextPaint textPaint = textView.getPaint();
                textPaint.setFakeBoldText(true);
                textView.setText(this.title);
            } else {
                layout.findViewById(R.id.tv_title).setVisibility(8);
                layout.findViewById(R.id.tv_content).setVisibility(8);
                layout.findViewById(R.id.tv_content_sigle).setVisibility(0);
            }
            dialog.setContentView(layout);
            if (this.isSingle) {
                layout.findViewById(R.id.rl_double).setVisibility(4);
                layout.findViewById(R.id.btn_single).setVisibility(0);
            } else {
                layout.findViewById(R.id.rl_double).setVisibility(0);
                layout.findViewById(R.id.btn_single).setVisibility(4);
            }
            if (this.clickOutIsCancle) {
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            } else {
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
            }
            if (this.isSpan) {
                ((TextView) layout.findViewById(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());
                ((TextView) layout.findViewById(R.id.tv_content)).setHighlightColor(0);
            }
            FontUtil.changeFontLanTing(this.context.getAssets(), layout.findViewById(R.id.btn_right), layout.findViewById(R.id.btn_left), layout.findViewById(R.id.btn_single), layout.findViewById(R.id.tv_title), layout.findViewById(R.id.tv_content), layout.findViewById(R.id.tv_content_sigle));
            if (this.rightButtonColor != -1) {
                ((Button) layout.findViewById(R.id.btn_right)).setTextColor(this.rightButtonColor);
            }
            if (this.leftButtonColor != -1) {
                ((Button) layout.findViewById(R.id.btn_left)).setTextColor(this.leftButtonColor);
            }
            if (this.singleButtonColor != -1) {
                ((Button) layout.findViewById(R.id.btn_single)).setTextColor(this.singleButtonColor);
            }
            if (this.gravity != -1) {
                ((TextView) layout.findViewById(R.id.tv_content)).setGravity(this.gravity);
            }
            if (this.isVerticalScreen) {
                dialog.getWindow().setGravity(80);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.y = 20;
                lp.width = (int) (AbViewUtil.getScreenWidth(this.context) * 0.95f);
                dialog.getWindow().setAttributes(lp);
            }
            if (!this.isShowVirtKey) {
                dialog.getWindow().getDecorView().setSystemUiVisibility(2);
                dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
                    int uiOptions;
                    if (Build.VERSION.SDK_INT >= 19) {
                        uiOptions = 1798 | 4096;
                    } else {
                        uiOptions = 1798 | 1;
                    }
                    dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                });
            }
            return dialog;
        }

        private void setBoldText(@NonNull TextView textView) {
            TextPaint tp = textView.getPaint();
            tp.setFakeBoldText(true);
        }
    }
}

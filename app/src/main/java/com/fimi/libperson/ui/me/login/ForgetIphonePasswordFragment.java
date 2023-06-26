package com.fimi.libperson.ui.me.login;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.base.BaseFragment;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.libperson.ivew.IForgetIphonePasswordView;
import com.fimi.libperson.presenter.ForgetIphonePasswordPresenter;


public class ForgetIphonePasswordFragment extends BaseFragment implements IForgetIphonePasswordView {
    private static final String TAG = "ForgetIphonePasswordFra";
    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 4 || actionId == 6 || (event != null && 66 == event.getKeyCode() && event.getAction() == 0)) {
                AbAppUtil.closeSoftInput(ForgetIphonePasswordFragment.this.mContext);
                return false;
            }
            return false;
        }
    };
    Button mBtnFiLoginIphone;
    EditText mEtFiAccount;
    EditText mEtFiVerification;
    EditText mEtNewPassword;
    EditText mEtNewPasswordAgain;
    ImageView mIvNewPasswordAgainUnified;
    ImageView mIvNewPasswordUnified;
    ImageView mIvShowPassword;
    ImageView mIvShowPasswordAgain;
    TextView mTvFiAreaCode;
    TextView mTvFiErrorHint;
    TextView mTvFiGetValidationCode;
    TextView mTvFiPasswordErrorHint;
    TextView mTvFiSelectCountry;
    TextView mTvFiTitleSubName;
    TextView mTvTitleSubName;
    View mVNpDivider;
    View mVNpDividerAgain;
    View mView1;
    View mView2;
    View mViewDivide;
    private boolean isShowPassword;
    private boolean isShowPasswordAgain;
    private ForgetIphonePasswordPresenter mForgetIphonePasswordPresenter;
    private OnResetIphonePasswordListerner mListerner;
    private State mState = State.IPHONE;

    public void setIphone(String iphone) {
        if (this.mEtFiAccount != null) {
            this.mEtFiAccount.setText(iphone);
        }
        showState();
    }

    public void setListerner(OnResetIphonePasswordListerner listerner) {
        this.mListerner = listerner;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mListerner = (OnResetIphonePasswordListerner) context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_forget_iphone;
    }

    @Override
    protected void initData(View view) {
        this.mTvFiTitleSubName = view.findViewById(R.id.tv_fi_title_sub_name);
        this.mTvFiSelectCountry = view.findViewById(R.id.tv_fi_select_country);
        this.mEtFiAccount = view.findViewById(R.id.et_fi_account);
        this.mEtFiVerification = view.findViewById(R.id.et_fi_verification);
        this.mTvFiGetValidationCode = view.findViewById(R.id.btn_fi_get_validation_code);
        this.mTvTitleSubName = view.findViewById(R.id.tv_title_sub_name);
        this.mTvFiErrorHint = view.findViewById(R.id.tv_fi_error_hint);
        this.mTvFiPasswordErrorHint = view.findViewById(R.id.tv_fi_password_error_hint);
        this.mBtnFiLoginIphone = view.findViewById(R.id.btn_fi_login_phone);
        this.mTvFiAreaCode = view.findViewById(R.id.tv_fi_area_code);
        this.mView1 = view.findViewById(R.id.view1);
        this.mView2 = view.findViewById(R.id.view2);
        this.mViewDivide = view.findViewById(R.id.v_divide);
        this.mEtNewPassword = view.findViewById(R.id.et_new_password);
        this.mEtNewPasswordAgain = view.findViewById(R.id.et_new_password_again);
        this.mIvNewPasswordUnified = view.findViewById(R.id.iv_new_password_unified);
        this.mIvNewPasswordAgainUnified = view.findViewById(R.id.iv_new_password_again_unified);
        this.mVNpDivider = view.findViewById(R.id.v_np_divider);
        this.mVNpDividerAgain = view.findViewById(R.id.v_np_again_divider);
        this.mIvShowPassword = view.findViewById(R.id.iv_show_password);
        this.mIvShowPasswordAgain = view.findViewById(R.id.iv_show_password_again);
        this.mTvFiSelectCountry.setText(getResources().getString(R.string.libperson_service_china));
        this.mTvFiGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
        this.mTvFiGetValidationCode.setEnabled(false);
        this.mIvNewPasswordUnified.setVisibility(View.GONE);
        this.mIvNewPasswordAgainUnified.setVisibility(View.GONE);
        this.mEtNewPassword.setVisibility(View.INVISIBLE);
        this.mEtNewPasswordAgain.setVisibility(View.INVISIBLE);
        this.mIvShowPassword.setVisibility(View.GONE);
        this.mIvShowPasswordAgain.setVisibility(View.GONE);
        this.mEtNewPassword.addTextChangedListener(new EditTextWatcher(this.mEtNewPassword));
        this.mEtNewPasswordAgain.addTextChangedListener(new EditTextWatcher(this.mEtNewPasswordAgain));
        this.mEtFiVerification.addTextChangedListener(new EditTextWatcher(this.mEtFiVerification));
        this.mEtFiAccount.addTextChangedListener(new EditTextWatcher(this.mEtFiAccount));
        this.mForgetIphonePasswordPresenter = new ForgetIphonePasswordPresenter(this, getActivity());
        showState();
    }

    @Override
    protected void doTrans() {
        OnClickListerner();
    }

    private void OnClickListerner() {
        this.mTvFiGetValidationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetIphonePasswordFragment.this.mContext);
                ForgetIphonePasswordFragment.this.mForgetIphonePasswordPresenter.sendIphone(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString());
            }
        });
        this.mBtnFiLoginIphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetIphonePasswordFragment.this.mContext);
                ForgetIphonePasswordFragment.this.mBtnFiLoginIphone.setEnabled(true);
                if (ForgetIphonePasswordFragment.this.mState == State.IPHONE) {
                    ForgetIphonePasswordFragment.this.mForgetIphonePasswordPresenter.inputVerficationCode(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString().trim(), ForgetIphonePasswordFragment.this.mEtFiVerification.getText().toString().trim());
                } else {
                    ForgetIphonePasswordFragment.this.mForgetIphonePasswordPresenter.inputPassword(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString().trim(), ForgetIphonePasswordFragment.this.mEtFiVerification.getText().toString().trim(), ForgetIphonePasswordFragment.this.mEtNewPassword.getText().toString().trim(), ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim());
                }
            }
        });
        this.mIvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ForgetIphonePasswordFragment.this.isShowPassword) {
                    ForgetIphonePasswordFragment.this.isShowPassword = false;
                    ForgetIphonePasswordFragment.this.mEtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ForgetIphonePasswordFragment.this.mIvShowPassword.setImageResource(R.drawable.iv_login_email_password);
                } else {
                    ForgetIphonePasswordFragment.this.isShowPassword = true;
                    ForgetIphonePasswordFragment.this.mEtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ForgetIphonePasswordFragment.this.mIvShowPassword.setImageResource(R.drawable.iv_login_email_password_show);
                }
                ForgetIphonePasswordFragment.this.mEtNewPassword.requestFocus();
                ForgetIphonePasswordFragment.this.mEtNewPassword.setSelection(ForgetIphonePasswordFragment.this.mEtNewPassword.getText().length());
            }
        });
        this.mIvShowPasswordAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ForgetIphonePasswordFragment.this.isShowPasswordAgain) {
                    ForgetIphonePasswordFragment.this.isShowPasswordAgain = false;
                    ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ForgetIphonePasswordFragment.this.mIvShowPasswordAgain.setImageResource(R.drawable.iv_login_email_password);
                } else {
                    ForgetIphonePasswordFragment.this.isShowPasswordAgain = true;
                    ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ForgetIphonePasswordFragment.this.mIvShowPasswordAgain.setImageResource(R.drawable.iv_login_email_password_show);
                }
                ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.requestFocus();
                ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.setSelection(ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().length());
            }
        });
        this.mEtFiVerification.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtFiAccount.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtNewPassword.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtNewPasswordAgain.setOnEditorActionListener(this.mOnEditorActionListener);
    }

    @Override
    protected void initMVP() {
    }

    @Override
    public void sendIphone(boolean isSuccess, String error) {
        this.mBtnFiLoginIphone.setEnabled(true);
        if (isSuccess) {
            this.mTvFiErrorHint.setText(null);
            showState();
        } else if (error != null) {
            this.mTvFiErrorHint.setText(error);
            this.mTvFiErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override
    public void sendVerfication(boolean isSuccess, String error) {
        if (isSuccess) {
            this.mTvFiErrorHint.setText(null);
            this.mTvFiGetValidationCode.setEnabled(true);
            this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            this.mForgetIphonePasswordPresenter.setStopTime();
            this.mTvFiPasswordErrorHint.setText(null);
            showClickState(false);
            this.mState = State.NEW_PASSWORD;
            showState();
        } else if (error != null) {
            this.mTvFiErrorHint.setText(error);
            this.mTvFiErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override
    public void resetPassword(boolean isSuccess, String error) {
        if (isSuccess) {
            if (this.mListerner != null) {
                this.mEtFiAccount.setText(null);
                this.mEtFiVerification.setText(null);
                this.mEtNewPassword.setText(null);
                this.mEtNewPasswordAgain.setText(null);
                this.mState = State.IPHONE;
                showState();
                this.mListerner.resetIphoneSuccess();
            }
        } else if (error != null) {
            this.mTvFiPasswordErrorHint.setText(error);
            this.mTvFiPasswordErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override
    public void updateSeconds(boolean isComplete, int seconds) {
        if (isComplete) {
            this.mTvFiGetValidationCode.setEnabled(true);
            this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            return;
        }
        this.mTvFiGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
        this.mTvFiGetValidationCode.setEnabled(false);
        this.mTvFiGetValidationCode.setText(seconds + getString(R.string.login_second));
    }

    private void showState() {
        if (this.mState == State.IPHONE) {
            this.mEtFiAccount.setVisibility(View.VISIBLE);
            this.mEtFiVerification.setVisibility(View.VISIBLE);
            this.mTvFiSelectCountry.setVisibility(View.VISIBLE);
            this.mView1.setVisibility(View.VISIBLE);
            this.mView2.setVisibility(View.VISIBLE);
            this.mTvFiAreaCode.setVisibility(View.VISIBLE);
            this.mTvFiGetValidationCode.setVisibility(View.VISIBLE);
            this.mBtnFiLoginIphone.setText(R.string.login_btn_next);
            this.mTvFiErrorHint.setVisibility(View.VISIBLE);
            this.mTvFiErrorHint.setVisibility(View.VISIBLE);
            this.mTvFiPasswordErrorHint.setVisibility(View.GONE);
            this.mIvShowPasswordAgain.setVisibility(View.INVISIBLE);
            this.mIvShowPassword.setVisibility(View.INVISIBLE);
            this.mIvNewPasswordUnified.setVisibility(View.INVISIBLE);
            this.mIvNewPasswordAgainUnified.setVisibility(View.INVISIBLE);
            this.mEtNewPassword.setVisibility(View.INVISIBLE);
            this.mEtNewPasswordAgain.setVisibility(View.INVISIBLE);
            this.mVNpDivider.setVisibility(View.INVISIBLE);
            this.mVNpDividerAgain.setVisibility(View.INVISIBLE);
            this.mViewDivide.setVisibility(View.INVISIBLE);
            showClickState(DataValidatorUtil.isMobile(this.mEtFiAccount.getText().toString().trim()) && this.mEtFiVerification.getText().length() == 4);
            if (DataValidatorUtil.isMobile(this.mEtFiAccount.getText().toString())) {
                Log.i(TAG, "showState: 1");
                this.mTvFiGetValidationCode.setEnabled(true);
                this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
                return;
            }
            Log.i(TAG, "showState: 2");
            this.mTvFiGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
            this.mTvFiGetValidationCode.setEnabled(false);
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mEtFiAccount.setVisibility(View.INVISIBLE);
            this.mEtFiVerification.setVisibility(View.INVISIBLE);
            this.mTvFiSelectCountry.setVisibility(View.INVISIBLE);
            this.mView1.setVisibility(View.INVISIBLE);
            this.mView2.setVisibility(View.INVISIBLE);
            this.mTvFiAreaCode.setVisibility(View.INVISIBLE);
            this.mTvFiGetValidationCode.setVisibility(View.INVISIBLE);
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            this.mTvFiErrorHint.setVisibility(View.INVISIBLE);
            this.mBtnFiLoginIphone.setText(R.string.login_reset_password);
            this.mTvFiPasswordErrorHint.setText(R.string.login_input_password_hint);
            this.mTvFiPasswordErrorHint.setTextColor(getResources().getColor(R.color.forget_password_hint));
            this.mTvFiPasswordErrorHint.setVisibility(View.VISIBLE);
            this.mViewDivide.setVisibility(View.VISIBLE);
            this.mIvShowPassword.setVisibility(View.VISIBLE);
            this.mIvShowPasswordAgain.setVisibility(View.VISIBLE);
            this.mEtNewPassword.setVisibility(View.VISIBLE);
            this.mEtNewPasswordAgain.setVisibility(View.VISIBLE);
            this.mVNpDivider.setVisibility(View.VISIBLE);
            this.mVNpDividerAgain.setVisibility(View.VISIBLE);
            setIvShowPassword(!this.mEtNewPasswordAgain.getText().toString().trim().equals(this.mEtNewPassword.getText().toString().trim()) || this.mEtNewPasswordAgain.getText().toString().length() < 8);
        }
    }

    public void setIvShowPassword(boolean isShowPassword) {
        this.mIvShowPassword.setVisibility(isShowPassword ? 0 : 8);
        this.mIvShowPasswordAgain.setVisibility(isShowPassword ? 0 : 8);
        this.mVNpDivider.setVisibility(isShowPassword ? 0 : 8);
        this.mVNpDividerAgain.setVisibility(isShowPassword ? 0 : 8);
        this.mIvNewPasswordUnified.setVisibility(isShowPassword ? 8 : 0);
        this.mIvNewPasswordAgainUnified.setVisibility(isShowPassword ? 8 : 0);
    }

    public State getState() {
        return this.mState;
    }

    public void setState(State state) {
        this.mState = state;
    }

    public void setBack() {
        if (this.mState == State.IPHONE) {
            this.mTvFiGetValidationCode.setEnabled(true);
            this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            this.mForgetIphonePasswordPresenter.setStopTime();
            this.mEtFiAccount.setText(null);
            this.mEtFiVerification.setText(null);
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mEtNewPassword.setText(null);
            this.mEtNewPasswordAgain.setText(null);
            this.mState = State.IPHONE;
            this.mEtFiVerification.setText(null);
            showState();
        }
    }

    public void showClickState(boolean isClick) {
        this.mBtnFiLoginIphone.setEnabled(isClick);
    }

    private SpannableString getSpannableString() {
        String str1 = this.mContext.getString(R.string.login_send_email_hint1);
        String str2 = this.mContext.getString(R.string.login_send_email_hint2);
        String str3 = this.mContext.getString(R.string.login_send_email_hint3);
        SpannableString spannableString = new SpannableString(str1 + str2 + str3);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length() + str3.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }

    private SpannableString getEmailVerficationSpannableString() {
        String str1 = this.mContext.getString(R.string.login_email_send_hint1);
        String str2 = this.mContext.getString(R.string.login_send_email_hint2);
        SpannableString spannableString = new SpannableString(str1 + str2);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }


    public enum State {
        IPHONE,
        NEW_PASSWORD
    }


    interface OnResetIphonePasswordListerner {
        void resetIphoneSuccess();
    }


    class EditTextWatcher implements TextWatcher {
        private final EditText mEditText;

        public EditTextWatcher(EditText editText) {
            this.mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if (R.id.et_fi_account == this.mEditText.getId()) {
                    if (!DataValidatorUtil.isMobile(s.toString().trim())) {
                        ForgetIphonePasswordFragment.this.mTvFiGetValidationCode.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.login_get_verfication_unclick));
                        ForgetIphonePasswordFragment.this.mTvFiGetValidationCode.setEnabled(false);
                        return;
                    }
                    ForgetIphonePasswordFragment.this.mTvFiGetValidationCode.setEnabled(true);
                    ForgetIphonePasswordFragment.this.mTvFiGetValidationCode.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
                } else if (R.id.et_fi_verification == this.mEditText.getId()) {
                    ForgetIphonePasswordFragment.this.showClickState(s.length() == 4 && DataValidatorUtil.isMobile(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString().trim()));
                    ForgetIphonePasswordFragment.this.mTvFiErrorHint.setText(null);
                } else if (R.id.et_new_password == this.mEditText.getId()) {
                    if (ForgetIphonePasswordFragment.this.mEtNewPassword.getText().toString().trim().equals(ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim()) && s.length() >= 8) {
                        ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(View.VISIBLE);
                        ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(View.VISIBLE);
                        ForgetIphonePasswordFragment.this.setIvShowPassword(false);
                        ForgetIphonePasswordFragment.this.showClickState(true);
                    } else {
                        ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(View.GONE);
                        ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(View.GONE);
                        ForgetIphonePasswordFragment.this.setIvShowPassword(true);
                        ForgetIphonePasswordFragment.this.showClickState(false);
                    }
                    ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setText(R.string.login_input_password_hint);
                    ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
                } else if (R.id.et_new_password_again == this.mEditText.getId()) {
                    if (ForgetIphonePasswordFragment.this.mEtNewPassword.getText().toString().trim().equals(ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim()) && s.length() >= 8) {
                        ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(View.VISIBLE);
                        ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(View.VISIBLE);
                        ForgetIphonePasswordFragment.this.setIvShowPassword(false);
                        ForgetIphonePasswordFragment.this.showClickState(true);
                    } else {
                        ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(View.GONE);
                        ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(View.GONE);
                        ForgetIphonePasswordFragment.this.setIvShowPassword(true);
                        ForgetIphonePasswordFragment.this.showClickState(false);
                    }
                    ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setText(R.string.login_input_password_hint);
                    ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
                }
            }
        }
    }
}

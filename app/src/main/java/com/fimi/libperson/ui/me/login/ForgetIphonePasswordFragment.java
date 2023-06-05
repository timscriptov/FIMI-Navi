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

/* loaded from: classes.dex */
public class ForgetIphonePasswordFragment extends BaseFragment implements IForgetIphonePasswordView {
    private static final String TAG = "ForgetIphonePasswordFra";
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
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() { // from class: com.fimi.libperson.ui.me.login.ForgetIphonePasswordFragment.5
        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 4 || actionId == 6 || (event != null && 66 == event.getKeyCode() && event.getAction() == 0)) {
                AbAppUtil.closeSoftInput(ForgetIphonePasswordFragment.this.mContext);
                return false;
            }
            return false;
        }
    };

    public void setIphone(String iphone) {
        if (this.mEtFiAccount != null) {
            this.mEtFiAccount.setText(iphone);
        }
        showState();
    }

    public void setListerner(OnResetIphonePasswordListerner listerner) {
        this.mListerner = listerner;
    }

    @Override // com.fimi.kernel.base.BaseFragment, android.support.v4.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mListerner = (OnResetIphonePasswordListerner) context;
    }

    @Override // com.fimi.kernel.base.BaseFragment
    public int getLayoutId() {
        return R.layout.fragment_forget_iphone;
    }

    @Override // com.fimi.kernel.base.BaseFragment
    protected void initData(View view) {
        this.mTvFiTitleSubName = (TextView) view.findViewById(R.id.tv_fi_title_sub_name);
        this.mTvFiSelectCountry = (TextView) view.findViewById(R.id.tv_fi_select_country);
        this.mEtFiAccount = (EditText) view.findViewById(R.id.et_fi_account);
        this.mEtFiVerification = (EditText) view.findViewById(R.id.et_fi_verification);
        this.mTvFiGetValidationCode = (TextView) view.findViewById(R.id.btn_fi_get_validation_code);
        this.mTvTitleSubName = (TextView) view.findViewById(R.id.tv_title_sub_name);
        this.mTvFiErrorHint = (TextView) view.findViewById(R.id.tv_fi_error_hint);
        this.mTvFiPasswordErrorHint = (TextView) view.findViewById(R.id.tv_fi_password_error_hint);
        this.mBtnFiLoginIphone = (Button) view.findViewById(R.id.btn_fi_login_phone);
        this.mTvFiAreaCode = (TextView) view.findViewById(R.id.tv_fi_area_code);
        this.mView1 = view.findViewById(R.id.view1);
        this.mView2 = view.findViewById(R.id.view2);
        this.mViewDivide = view.findViewById(R.id.v_divide);
        this.mEtNewPassword = (EditText) view.findViewById(R.id.et_new_password);
        this.mEtNewPasswordAgain = (EditText) view.findViewById(R.id.et_new_password_again);
        this.mIvNewPasswordUnified = (ImageView) view.findViewById(R.id.iv_new_password_unified);
        this.mIvNewPasswordAgainUnified = (ImageView) view.findViewById(R.id.iv_new_password_again_unified);
        this.mVNpDivider = view.findViewById(R.id.v_np_divider);
        this.mVNpDividerAgain = view.findViewById(R.id.v_np_again_divider);
        this.mIvShowPassword = (ImageView) view.findViewById(R.id.iv_show_password);
        this.mIvShowPasswordAgain = (ImageView) view.findViewById(R.id.iv_show_password_again);
        this.mTvFiSelectCountry.setText(getResources().getString(R.string.libperson_service_china));
        this.mTvFiGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
        this.mTvFiGetValidationCode.setEnabled(false);
        this.mIvNewPasswordUnified.setVisibility(8);
        this.mIvNewPasswordAgainUnified.setVisibility(8);
        this.mEtNewPassword.setVisibility(4);
        this.mEtNewPasswordAgain.setVisibility(4);
        this.mIvShowPassword.setVisibility(8);
        this.mIvShowPasswordAgain.setVisibility(8);
        this.mEtNewPassword.addTextChangedListener(new EditTextWatcher(this.mEtNewPassword));
        this.mEtNewPasswordAgain.addTextChangedListener(new EditTextWatcher(this.mEtNewPasswordAgain));
        this.mEtFiVerification.addTextChangedListener(new EditTextWatcher(this.mEtFiVerification));
        this.mEtFiAccount.addTextChangedListener(new EditTextWatcher(this.mEtFiAccount));
        this.mForgetIphonePasswordPresenter = new ForgetIphonePasswordPresenter(this, getActivity());
        showState();
    }

    @Override // com.fimi.kernel.base.BaseFragment
    protected void doTrans() {
        OnClickListerner();
    }

    private void OnClickListerner() {
        this.mTvFiGetValidationCode.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.ForgetIphonePasswordFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetIphonePasswordFragment.this.mContext);
                ForgetIphonePasswordFragment.this.mForgetIphonePasswordPresenter.sendIphone(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString());
            }
        });
        this.mBtnFiLoginIphone.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.ForgetIphonePasswordFragment.2
            @Override // android.view.View.OnClickListener
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
        this.mIvShowPassword.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.ForgetIphonePasswordFragment.3
            @Override // android.view.View.OnClickListener
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
        this.mIvShowPasswordAgain.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.ForgetIphonePasswordFragment.4
            @Override // android.view.View.OnClickListener
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

    @Override // com.fimi.kernel.base.BaseFragment
    protected void initMVP() {
    }

    @Override // com.fimi.libperson.ivew.IForgetIphonePasswordView
    public void sendIphone(boolean isSuccess, String error) {
        this.mBtnFiLoginIphone.setEnabled(true);
        if (isSuccess) {
            this.mTvFiErrorHint.setText((CharSequence) null);
            showState();
        } else if (error != null) {
            this.mTvFiErrorHint.setText(error);
            this.mTvFiErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override // com.fimi.libperson.ivew.IForgetIphonePasswordView
    public void sendVerfication(boolean isSuccess, String error) {
        if (isSuccess) {
            this.mTvFiErrorHint.setText((CharSequence) null);
            this.mTvFiGetValidationCode.setEnabled(true);
            this.mTvFiGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            this.mForgetIphonePasswordPresenter.setStopTime();
            this.mTvFiPasswordErrorHint.setText((CharSequence) null);
            showClickState(false);
            this.mState = State.NEW_PASSWORD;
            showState();
        } else if (error != null) {
            this.mTvFiErrorHint.setText(error);
            this.mTvFiErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override // com.fimi.libperson.ivew.IForgetIphonePasswordView
    public void resetPassword(boolean isSuccess, String error) {
        if (isSuccess) {
            if (this.mListerner != null) {
                this.mEtFiAccount.setText((CharSequence) null);
                this.mEtFiVerification.setText((CharSequence) null);
                this.mEtNewPassword.setText((CharSequence) null);
                this.mEtNewPasswordAgain.setText((CharSequence) null);
                this.mState = State.IPHONE;
                showState();
                this.mListerner.resetIphoneSuccess();
            }
        } else if (error != null) {
            this.mTvFiPasswordErrorHint.setText(error);
            this.mTvFiPasswordErrorHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override // com.fimi.libperson.ivew.IForgetIphonePasswordView
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
            this.mEtFiAccount.setVisibility(0);
            this.mEtFiVerification.setVisibility(0);
            this.mTvFiSelectCountry.setVisibility(0);
            this.mView1.setVisibility(0);
            this.mView2.setVisibility(0);
            this.mTvFiAreaCode.setVisibility(0);
            this.mTvFiGetValidationCode.setVisibility(0);
            this.mBtnFiLoginIphone.setText(R.string.login_btn_next);
            this.mTvFiErrorHint.setVisibility(0);
            this.mTvFiErrorHint.setVisibility(0);
            this.mTvFiPasswordErrorHint.setVisibility(8);
            this.mIvShowPasswordAgain.setVisibility(4);
            this.mIvShowPassword.setVisibility(4);
            this.mIvNewPasswordUnified.setVisibility(4);
            this.mIvNewPasswordAgainUnified.setVisibility(4);
            this.mEtNewPassword.setVisibility(4);
            this.mEtNewPasswordAgain.setVisibility(4);
            this.mVNpDivider.setVisibility(4);
            this.mVNpDividerAgain.setVisibility(4);
            this.mViewDivide.setVisibility(4);
            if (DataValidatorUtil.isMobile(this.mEtFiAccount.getText().toString().trim()) && this.mEtFiVerification.getText().length() == 4) {
                showClickState(true);
            } else {
                showClickState(false);
            }
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
            this.mEtFiAccount.setVisibility(4);
            this.mEtFiVerification.setVisibility(4);
            this.mTvFiSelectCountry.setVisibility(4);
            this.mView1.setVisibility(4);
            this.mView2.setVisibility(4);
            this.mTvFiAreaCode.setVisibility(4);
            this.mTvFiGetValidationCode.setVisibility(4);
            this.mTvFiGetValidationCode.setText(R.string.login_btn_verification);
            this.mTvFiErrorHint.setVisibility(4);
            this.mBtnFiLoginIphone.setText(R.string.login_reset_password);
            this.mTvFiPasswordErrorHint.setText(R.string.login_input_password_hint);
            this.mTvFiPasswordErrorHint.setTextColor(getResources().getColor(R.color.forget_password_hint));
            this.mTvFiPasswordErrorHint.setVisibility(0);
            this.mViewDivide.setVisibility(0);
            this.mIvShowPassword.setVisibility(0);
            this.mIvShowPasswordAgain.setVisibility(0);
            this.mEtNewPassword.setVisibility(0);
            this.mEtNewPasswordAgain.setVisibility(0);
            this.mVNpDivider.setVisibility(0);
            this.mVNpDividerAgain.setVisibility(0);
            if (this.mEtNewPasswordAgain.getText().toString().trim().equals(this.mEtNewPassword.getText().toString().trim()) && this.mEtNewPasswordAgain.getText().toString().length() >= 8) {
                setIvShowPassword(false);
            } else {
                setIvShowPassword(true);
            }
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
            this.mEtFiAccount.setText((CharSequence) null);
            this.mEtFiVerification.setText((CharSequence) null);
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mEtNewPassword.setText((CharSequence) null);
            this.mEtNewPasswordAgain.setText((CharSequence) null);
            this.mState = State.IPHONE;
            this.mEtFiVerification.setText((CharSequence) null);
            showState();
        }
    }

    public void showClickState(boolean isClick) {
        if (isClick) {
            this.mBtnFiLoginIphone.setEnabled(true);
        } else {
            this.mBtnFiLoginIphone.setEnabled(false);
        }
    }

    private SpannableString getSpannableString() {
        String str1 = this.mContext.getString(R.string.login_send_email_hint1);
        String str2 = this.mContext.getString(R.string.login_send_email_hint2);
        String str3 = this.mContext.getString(R.string.login_send_email_hint3);
        SpannableString spannableString = new SpannableString(str1 + str2 + str3);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length() + str3.length(), 33);
        spannableString.setSpan(new ClickableSpan() { // from class: com.fimi.libperson.ui.me.login.ForgetIphonePasswordFragment.6
            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override // android.text.style.ClickableSpan
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
        spannableString.setSpan(new ClickableSpan() { // from class: com.fimi.libperson.ui.me.login.ForgetIphonePasswordFragment.7
            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override // android.text.style.ClickableSpan
            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }

    /* loaded from: classes.dex */
    public enum State {
        IPHONE,
        NEW_PASSWORD
    }

    /* loaded from: classes.dex */
    interface OnResetIphonePasswordListerner {
        void resetIphoneSuccess();
    }

    /* loaded from: classes.dex */
    class EditTextWatcher implements TextWatcher {
        private EditText mEditText;

        public EditTextWatcher(EditText editText) {
            this.mEditText = editText;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override // android.text.TextWatcher
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
                    if (s.length() != 4 || !DataValidatorUtil.isMobile(ForgetIphonePasswordFragment.this.mEtFiAccount.getText().toString().trim())) {
                        ForgetIphonePasswordFragment.this.showClickState(false);
                    } else {
                        ForgetIphonePasswordFragment.this.showClickState(true);
                    }
                    ForgetIphonePasswordFragment.this.mTvFiErrorHint.setText((CharSequence) null);
                } else if (R.id.et_new_password == this.mEditText.getId()) {
                    if (ForgetIphonePasswordFragment.this.mEtNewPassword.getText().toString().trim().equals(ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim()) && s.length() >= 8) {
                        ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(0);
                        ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(0);
                        ForgetIphonePasswordFragment.this.setIvShowPassword(false);
                        ForgetIphonePasswordFragment.this.showClickState(true);
                    } else {
                        ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(8);
                        ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(8);
                        ForgetIphonePasswordFragment.this.setIvShowPassword(true);
                        ForgetIphonePasswordFragment.this.showClickState(false);
                    }
                    ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setText(R.string.login_input_password_hint);
                    ForgetIphonePasswordFragment.this.mTvFiPasswordErrorHint.setTextColor(ForgetIphonePasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
                } else if (R.id.et_new_password_again == this.mEditText.getId()) {
                    if (ForgetIphonePasswordFragment.this.mEtNewPassword.getText().toString().trim().equals(ForgetIphonePasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim()) && s.length() >= 8) {
                        ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(0);
                        ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(0);
                        ForgetIphonePasswordFragment.this.setIvShowPassword(false);
                        ForgetIphonePasswordFragment.this.showClickState(true);
                    } else {
                        ForgetIphonePasswordFragment.this.mIvNewPasswordUnified.setVisibility(8);
                        ForgetIphonePasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(8);
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

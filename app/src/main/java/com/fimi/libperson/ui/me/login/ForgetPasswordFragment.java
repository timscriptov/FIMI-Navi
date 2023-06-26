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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.base.BaseFragment;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.libperson.ivew.IForgetPasswordView;
import com.fimi.libperson.presenter.ForgetPasswordPresenter;


public class ForgetPasswordFragment extends BaseFragment implements IForgetPasswordView {
    Button mBtnSendEmail;
    Button mBtnVerfication;
    EditText mEtFpEmailAccount;
    EditText mEtInputVerficationCode;
    EditText mEtNewPassword;
    EditText mEtNewPasswordAgain;
    ImageView mIvNewPasswordAgainUnified;
    ImageView mIvNewPasswordUnified;
    ImageView mIvShowPassword;
    ImageView mIvShowPasswordAgain;
    TextView mTvEmailaddress;
    TextView mTvFpHint;
    TextView mTvFpVerficationHint;
    TextView mTvTitleSubNmae;
    View mVNpDivider;
    View mVNpDividerAgain;
    View mViewDivide;
    private boolean isShowPassword;
    private boolean isShowPasswordAgain;
    private String mEmailAddressStr;
    private ForgetPasswordPresenter mForgetPasswordPresenter;
    private OnResetPasswordListerner mOnResetPasswordListerner;
    private State mState = State.EMAIL;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mOnResetPasswordListerner = (OnResetPasswordListerner) context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_forget_email;
    }

    @Override
    protected void initData(View view) {
        this.mEtFpEmailAccount = view.findViewById(R.id.et_fp_email_account);
        this.mEtNewPassword = view.findViewById(R.id.et_new_password);
        this.mEtNewPasswordAgain = view.findViewById(R.id.et_new_password_again);
        this.mViewDivide = view.findViewById(R.id.v_divide);
        this.mIvNewPasswordUnified = view.findViewById(R.id.iv_new_password_unified);
        this.mIvNewPasswordAgainUnified = view.findViewById(R.id.iv_new_password_again_unified);
        this.mEtInputVerficationCode = view.findViewById(R.id.et_input_verfication_code);
        this.mTvFpHint = view.findViewById(R.id.tv_fp_hint);
        this.mTvFpVerficationHint = view.findViewById(R.id.tv_fp_verfication_hint);
        this.mBtnSendEmail = view.findViewById(R.id.btn_send_email);
        this.mBtnVerfication = view.findViewById(R.id.btn_verfication);
        this.mTvEmailaddress = view.findViewById(R.id.tv_emailaddress);
        this.mTvTitleSubNmae = view.findViewById(R.id.tv_title_sub_name);
        this.mVNpDivider = view.findViewById(R.id.v_np_divider);
        this.mVNpDividerAgain = view.findViewById(R.id.v_np_again_divider);
        this.mIvShowPassword = view.findViewById(R.id.iv_show_password);
        this.mIvShowPasswordAgain = view.findViewById(R.id.iv_show_password_again);
        FontUtil.changeFontLanTing(getActivity().getAssets(), this.mTvEmailaddress, this.mTvFpHint, this.mEtFpEmailAccount, this.mEtNewPasswordAgain, this.mEtNewPassword, this.mEtInputVerficationCode, this.mBtnVerfication, this.mTvTitleSubNmae);
        this.mIvNewPasswordUnified.setVisibility(View.GONE);
        this.mIvNewPasswordAgainUnified.setVisibility(View.GONE);
        this.mTvEmailaddress.setVisibility(View.INVISIBLE);
        this.mEtNewPassword.setVisibility(View.INVISIBLE);
        this.mEtNewPasswordAgain.setVisibility(View.INVISIBLE);
        this.mViewDivide.setVisibility(View.INVISIBLE);
        this.mEtInputVerficationCode.setVisibility(View.INVISIBLE);
        this.mEtFpEmailAccount.addTextChangedListener(new EditTextWatcher(this.mEtFpEmailAccount));
        this.mEtInputVerficationCode.addTextChangedListener(new EditTextWatcher(this.mEtInputVerficationCode));
        this.mEtNewPassword.addTextChangedListener(new EditTextWatcher(this.mEtNewPassword));
        this.mEtNewPasswordAgain.addTextChangedListener(new EditTextWatcher(this.mEtNewPasswordAgain));
        this.mForgetPasswordPresenter = new ForgetPasswordPresenter(this, getActivity());
        showState();
    }

    @Override
    protected void doTrans() {
        OnClickListerner();
    }

    private void OnClickListerner() {
        this.mBtnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetPasswordFragment.this.mContext);
                ForgetPasswordFragment.this.mBtnSendEmail.setEnabled(false);
                if (ForgetPasswordFragment.this.mState == State.EMAIL) {
                    ForgetPasswordFragment.this.mEmailAddressStr = ForgetPasswordFragment.this.mEtFpEmailAccount.getText().toString();
                    ForgetPasswordFragment.this.mForgetPasswordPresenter.sendEmail(ForgetPasswordFragment.this.mEmailAddressStr);
                }
            }
        });
        this.mBtnVerfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(ForgetPasswordFragment.this.mContext);
                ForgetPasswordFragment.this.mBtnVerfication.setEnabled(false);
                if (ForgetPasswordFragment.this.mState == State.VERIFICATION_CODE) {
                    ForgetPasswordFragment.this.mForgetPasswordPresenter.inputVerficationCode(ForgetPasswordFragment.this.mEmailAddressStr, ForgetPasswordFragment.this.mEtInputVerficationCode.getText().toString());
                } else if (ForgetPasswordFragment.this.mState == State.NEW_PASSWORD) {
                    ForgetPasswordFragment.this.mForgetPasswordPresenter.inputPassword(ForgetPasswordFragment.this.mEmailAddressStr, ForgetPasswordFragment.this.mEtInputVerficationCode.getText().toString(), ForgetPasswordFragment.this.mEtNewPassword.getText().toString(), ForgetPasswordFragment.this.mEtNewPasswordAgain.getText().toString());
                }
            }
        });
        this.mIvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ForgetPasswordFragment.this.isShowPassword) {
                    ForgetPasswordFragment.this.isShowPassword = false;
                    ForgetPasswordFragment.this.mEtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ForgetPasswordFragment.this.mIvShowPassword.setImageResource(R.drawable.iv_login_email_password);
                } else {
                    ForgetPasswordFragment.this.isShowPassword = true;
                    ForgetPasswordFragment.this.mEtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ForgetPasswordFragment.this.mIvShowPassword.setImageResource(R.drawable.iv_login_email_password_show);
                }
                ForgetPasswordFragment.this.mEtNewPassword.requestFocus();
                ForgetPasswordFragment.this.mEtNewPassword.setSelection(ForgetPasswordFragment.this.mEtNewPassword.getText().length());
            }
        });
        this.mIvShowPasswordAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ForgetPasswordFragment.this.isShowPasswordAgain) {
                    ForgetPasswordFragment.this.isShowPasswordAgain = false;
                    ForgetPasswordFragment.this.mEtNewPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ForgetPasswordFragment.this.mIvShowPasswordAgain.setImageResource(R.drawable.iv_login_email_password);
                } else {
                    ForgetPasswordFragment.this.isShowPasswordAgain = true;
                    ForgetPasswordFragment.this.mEtNewPasswordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ForgetPasswordFragment.this.mIvShowPasswordAgain.setImageResource(R.drawable.iv_login_email_password_show);
                }
                ForgetPasswordFragment.this.mEtNewPasswordAgain.requestFocus();
                ForgetPasswordFragment.this.mEtNewPasswordAgain.setSelection(ForgetPasswordFragment.this.mEtNewPasswordAgain.getText().length());
            }
        });
    }

    @Override
    protected void initMVP() {
    }

    @Override
    public void sendEmail(boolean isSuccess, String error) {
        this.mBtnSendEmail.setEnabled(true);
        if (isSuccess) {
            this.mState = State.VERIFICATION_CODE;
            this.mTvFpHint.setText(getEmailVerficationSpannableString());
            showState();
        } else if (error != null) {
            this.mTvFpHint.setText(error);
            this.mTvFpHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override
    public void inputVerfication(boolean isSuccess, String error) {
        this.mBtnVerfication.setEnabled(true);
        if (isSuccess) {
            this.mState = State.NEW_PASSWORD;
            showState();
            showClickState(false);
            this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
            this.mTvFpVerficationHint.setTextColor(getResources().getColor(R.color.forget_password_hint));
        } else if (error != null) {
            this.mTvFpVerficationHint.setText(error);
            this.mTvFpVerficationHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    @Override
    public void resetPassword(boolean isSuccess, String error) {
        this.mBtnVerfication.setEnabled(true);
        if (isSuccess) {
            if (this.mOnResetPasswordListerner != null) {
                this.mEtFpEmailAccount.setText(null);
                this.mEtInputVerficationCode.setText(null);
                this.mEtNewPassword.setText(null);
                this.mEtNewPasswordAgain.setText(null);
                this.mState = State.EMAIL;
                showState();
                this.mOnResetPasswordListerner.resetSuccess();
            }
        } else if (error != null) {
            this.mTvFpVerficationHint.setText(error);
            this.mTvFpVerficationHint.setTextColor(getResources().getColor(R.color.forget_password_error_hint));
        }
    }

    private void showState() {
        if (this.mState == State.EMAIL) {
            this.mEtFpEmailAccount.setVisibility(View.VISIBLE);
            this.mEtInputVerficationCode.setVisibility(View.INVISIBLE);
            this.mTvEmailaddress.setVisibility(View.GONE);
            this.mTvEmailaddress.setText("");
            this.mEtNewPassword.setVisibility(View.INVISIBLE);
            this.mEtNewPasswordAgain.setVisibility(View.INVISIBLE);
            this.mViewDivide.setVisibility(View.INVISIBLE);
            this.mBtnSendEmail.setVisibility(View.VISIBLE);
            this.mBtnVerfication.setVisibility(View.GONE);
            this.mIvNewPasswordUnified.setVisibility(View.GONE);
            this.mIvNewPasswordAgainUnified.setVisibility(View.GONE);
            this.mTvFpHint.setTextColor(getResources().getColor(R.color.forget_password_hint));
            this.mTvFpHint.setText(getSpannableString());
            this.mTvFpVerficationHint.setVisibility(View.GONE);
            this.mTvFpHint.setVisibility(View.VISIBLE);
            setIvShowPassword(false);
            showClickState(DataValidatorUtil.isEmail(this.mEtFpEmailAccount.getText().toString().trim()));
        } else if (this.mState == State.VERIFICATION_CODE) {
            this.mEtFpEmailAccount.setVisibility(View.INVISIBLE);
            this.mEtInputVerficationCode.setVisibility(View.VISIBLE);
            this.mTvEmailaddress.setVisibility(View.VISIBLE);
            this.mTvEmailaddress.setText(this.mEmailAddressStr);
            this.mEtNewPassword.setVisibility(View.INVISIBLE);
            this.mViewDivide.setVisibility(View.VISIBLE);
            this.mEtNewPasswordAgain.setVisibility(View.INVISIBLE);
            this.mBtnVerfication.setText(R.string.login_ensure);
            this.mBtnSendEmail.setVisibility(View.GONE);
            this.mBtnVerfication.setVisibility(View.VISIBLE);
            this.mIvNewPasswordUnified.setVisibility(View.GONE);
            this.mIvNewPasswordAgainUnified.setVisibility(View.GONE);
            this.mTvFpVerficationHint.setText(getEmailVerficationSpannableString());
            this.mTvFpVerficationHint.setVisibility(View.VISIBLE);
            this.mTvFpHint.setVisibility(View.GONE);
            setIvShowPassword(false);
            showBtnVerficationClickState(this.mEtInputVerficationCode.getText().length() == 6);
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mEtFpEmailAccount.setVisibility(View.INVISIBLE);
            this.mEtInputVerficationCode.setVisibility(View.INVISIBLE);
            this.mTvEmailaddress.setVisibility(View.GONE);
            this.mEtNewPassword.setVisibility(View.VISIBLE);
            this.mViewDivide.setVisibility(View.VISIBLE);
            this.mEtNewPasswordAgain.setVisibility(View.VISIBLE);
            this.mBtnVerfication.setText(R.string.login_reset_password);
            this.mBtnSendEmail.setVisibility(View.GONE);
            this.mBtnVerfication.setVisibility(View.VISIBLE);
            showClickState(false);
            this.mTvFpVerficationHint.setVisibility(View.VISIBLE);
            this.mTvFpHint.setVisibility(View.GONE);
            setIvShowPassword(true);
            showBtnVerficationClickState(this.mEtNewPasswordAgain.getText().toString().trim().equals(this.mEtNewPassword.getText().toString().trim()) && this.mEtNewPasswordAgain.getText().toString().length() >= 8);
        }
    }

    public void setIvShowPassword(boolean isShowPassword) {
        this.mIvShowPassword.setVisibility(isShowPassword ? 0 : 8);
        this.mIvShowPasswordAgain.setVisibility(isShowPassword ? 0 : 8);
        this.mVNpDivider.setVisibility(isShowPassword ? 0 : 8);
        this.mVNpDividerAgain.setVisibility(isShowPassword ? 0 : 8);
    }

    public void setEmailAddress(String emailAddress) {
        if (this.mEtFpEmailAccount != null) {
            this.mEtFpEmailAccount.setText(emailAddress);
        }
    }

    public State getState() {
        return this.mState;
    }

    public void setState(State state) {
        this.mState = state;
    }

    public void setBack() {
        if (this.mState == State.EMAIL) {
            this.mEtFpEmailAccount.setText(null);
        } else if (this.mState == State.VERIFICATION_CODE) {
            this.mState = State.EMAIL;
            this.mEtInputVerficationCode.setText(null);
            this.mTvEmailaddress.setText(null);
            showState();
        } else if (this.mState == State.NEW_PASSWORD) {
            this.mState = State.VERIFICATION_CODE;
            this.mEtNewPassword.setText(null);
            this.mEtNewPasswordAgain.setText(null);
            showState();
        }
    }

    public void showClickState(boolean isClick) {
        this.mBtnSendEmail.setEnabled(isClick);
    }

    public void showBtnVerficationClickState(boolean isClick) {
        this.mBtnVerfication.setEnabled(isClick);
    }

    public SpannableString getSpannableString() {
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
                ds.setColor(ForgetPasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }

    public SpannableString getEmailVerficationSpannableString() {
        String str1 = this.mContext.getString(R.string.login_email_send_hint1);
        String str2 = this.mContext.getString(R.string.login_send_email_hint2);
        SpannableString spannableString = new SpannableString(str1 + str2);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ForgetPasswordFragment.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        return spannableString;
    }


    public enum State {
        EMAIL,
        VERIFICATION_CODE,
        NEW_PASSWORD
    }


    interface OnResetPasswordListerner {
        void resetSuccess();
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
                if (R.id.et_fp_email_account == this.mEditText.getId()) {
                    ForgetPasswordFragment.this.showClickState(DataValidatorUtil.isEmail(this.mEditText.getText().toString().trim()));
                    ForgetPasswordFragment.this.mTvFpHint.setText(ForgetPasswordFragment.this.getSpannableString());
                } else if (R.id.et_input_verfication_code == this.mEditText.getId()) {
                    ForgetPasswordFragment.this.showBtnVerficationClickState(s.length() == 6);
                    ForgetPasswordFragment.this.mTvFpVerficationHint.setText(ForgetPasswordFragment.this.getEmailVerficationSpannableString());
                } else if (R.id.et_new_password == this.mEditText.getId()) {
                    if (ForgetPasswordFragment.this.mEtNewPassword.getText().toString().trim().equals(ForgetPasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim()) && s.length() >= 8) {
                        ForgetPasswordFragment.this.mIvNewPasswordUnified.setVisibility(View.VISIBLE);
                        ForgetPasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(View.VISIBLE);
                        ForgetPasswordFragment.this.setIvShowPassword(false);
                        ForgetPasswordFragment.this.showBtnVerficationClickState(true);
                    } else {
                        ForgetPasswordFragment.this.mIvNewPasswordUnified.setVisibility(View.GONE);
                        ForgetPasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(View.GONE);
                        ForgetPasswordFragment.this.setIvShowPassword(true);
                        ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                    }
                    ForgetPasswordFragment.this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
                    ForgetPasswordFragment.this.mTvFpHint.setTextColor(ForgetPasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
                } else if (R.id.et_new_password_again == this.mEditText.getId()) {
                    if (ForgetPasswordFragment.this.mEtNewPasswordAgain.getText().toString().trim().equals(ForgetPasswordFragment.this.mEtNewPassword.getText().toString().trim()) && s.length() >= 8) {
                        ForgetPasswordFragment.this.mIvNewPasswordUnified.setVisibility(View.VISIBLE);
                        ForgetPasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(View.VISIBLE);
                        ForgetPasswordFragment.this.setIvShowPassword(false);
                        ForgetPasswordFragment.this.showBtnVerficationClickState(true);
                    } else {
                        ForgetPasswordFragment.this.mIvNewPasswordUnified.setVisibility(View.GONE);
                        ForgetPasswordFragment.this.mIvNewPasswordAgainUnified.setVisibility(View.GONE);
                        ForgetPasswordFragment.this.setIvShowPassword(true);
                        ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                    }
                    ForgetPasswordFragment.this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
                    ForgetPasswordFragment.this.mTvFpHint.setTextColor(ForgetPasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
                }
            } else if (R.id.et_fp_email_account == this.mEditText.getId()) {
                ForgetPasswordFragment.this.mTvFpHint.setText(ForgetPasswordFragment.this.getSpannableString());
            } else if (R.id.et_input_verfication_code == this.mEditText.getId()) {
                ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setText(ForgetPasswordFragment.this.getEmailVerficationSpannableString());
            } else if (R.id.et_new_password == this.mEditText.getId()) {
                ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setTextColor(ForgetPasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
            } else if (R.id.et_new_password_again == this.mEditText.getId()) {
                ForgetPasswordFragment.this.showBtnVerficationClickState(false);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setText(R.string.login_input_password_hint);
                ForgetPasswordFragment.this.mTvFpVerficationHint.setTextColor(ForgetPasswordFragment.this.getResources().getColor(R.color.forget_password_hint));
            }
        }
    }
}

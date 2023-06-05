package com.fimi.libperson.ui.me.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.host.ComonStaticURL;
import com.fimi.kernel.Constants;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.ivew.IRegisterView;
import com.fimi.libperson.presenter.RegisterPrenster;
import com.fimi.libperson.ui.web.UserProtocolWebViewActivity;
import com.fimi.libperson.widget.TitleView;

import router.Router;


public class RegisterActivity extends BasePersonActivity implements IRegisterView {
    Button mBtnRegisterEmail;
    Button mBtnRegisterPhone;
    CheckBox mCbEmailSelectService;
    CheckBox mCbIphoneSelectService;
    EditText mEtAccount;
    EditText mEtEmailAccount;
    EditText mEtEmailPassword;
    EditText mEtGetVelidationCode;
    EditText mEtIphonePassword;
    ImageView mIvReturn;
    ImageView mIvShowIphonePassword;
    ImageView mIvShowPassword;
    RelativeLayout mRlEmail;
    RelativeLayout mRlIphone;
    TitleView mTitleView;
    TextView mTvAreaCode;
    TextView mTvEmailErrorHint;
    TextView mTvEmailSelectService;
    TextView mTvErrorHint;
    TextView mTvForgetHint;
    TextView mTvGetValidationCode;
    TextView mTvIphoneSelectService;
    TextView mTvSelectCountry;
    TextView mTvTitleNmae;
    RegisterPrenster registerPrenster;
    boolean isShowPassword = false;
    boolean isShowIphonePassword = false;
    boolean isEmail = false;
    private boolean isCheckEmail = false;
    private boolean isCheckPhone = false;
    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 4 || actionId == 6 || (event != null && 66 == event.getKeyCode() && event.getAction() == 0)) {
                AbAppUtil.closeSoftInput(RegisterActivity.this.mContext);
                return false;
            }
            return false;
        }
    };

    @Override
    public void setStatusBarColor() {
        super.setStatusBarColor();
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    public void initData() {
        initView();
        ServiceItem serviceItem = SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class);
        if (serviceItem != null && serviceItem.getInfo() != R.string.region_Mainland_China) {
            this.isEmail = true;
            this.mTitleView.setTvRightText(getString(R.string.login_iphone_title));
            this.mTvTitleNmae.setText(R.string.login_register_main_email_title);
            this.mTitleView.setRightTvIsVisible(false);
            this.mRlEmail.setVisibility(View.VISIBLE);
            this.mRlIphone.setVisibility(View.INVISIBLE);
            this.mBtnRegisterPhone.setVisibility(View.GONE);
            this.mBtnRegisterEmail.setVisibility(View.VISIBLE);
            if (this.isCheckEmail) {
                if (this.mEtEmailPassword.getText().length() >= 8 && this.mEtEmailPassword.getText().length() <= 16) {
                    registerBtnIsClick(true);
                    return;
                } else {
                    registerBtnIsClick(false);
                    return;
                }
            }
            registerBtnIsClick(false);
        }
    }

    private void initView() {
        this.registerPrenster = new RegisterPrenster(this, this);
        this.mIvReturn = findViewById(R.id.iv_return);
        this.mTitleView = findViewById(R.id.title_view);
        this.mTvSelectCountry = findViewById(R.id.tv_select_country);
        this.mTvAreaCode = findViewById(R.id.tv_area_code);
        this.mEtAccount = findViewById(R.id.et_account);
        this.mEtIphonePassword = findViewById(R.id.et_password);
        this.mEtGetVelidationCode = findViewById(R.id.et_verification);
        this.mTvGetValidationCode = findViewById(R.id.btn_get_validation_code);
        this.mTvErrorHint = findViewById(R.id.tv_error_hint);
        this.mCbIphoneSelectService = findViewById(R.id.cb_iphone_select_service);
        this.mCbEmailSelectService = findViewById(R.id.cb_email_select_service);
        this.mEtEmailAccount = findViewById(R.id.et_email_account);
        this.mEtEmailPassword = findViewById(R.id.et_email_password);
        this.mTvEmailErrorHint = findViewById(R.id.tv_email_error_hint);
        this.mTvForgetHint = findViewById(R.id.tv_forget_hint);
        this.mBtnRegisterPhone = findViewById(R.id.btn_register_phone);
        this.mBtnRegisterEmail = findViewById(R.id.btn_register_email);
        this.mRlIphone = findViewById(R.id.rl_iphone);
        this.mRlEmail = findViewById(R.id.rl_email);
        this.mIvShowPassword = findViewById(R.id.iv_show_password);
        this.mIvShowIphonePassword = findViewById(R.id.iv_show_iphone_password);
        this.mTvIphoneSelectService = findViewById(R.id.tv_iphone_select_service);
        this.mTvEmailSelectService = findViewById(R.id.tv_email_select_service);
        this.mTvTitleNmae = findViewById(R.id.tv_title_name);
        this.mTvGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
        this.mTvGetValidationCode.setEnabled(false);
        this.mTvIphoneSelectService.setVisibility(View.VISIBLE);
        this.mCbIphoneSelectService.setVisibility(View.VISIBLE);
        this.mCbIphoneSelectService.setChecked(false);
        this.mTvEmailSelectService.setVisibility(View.VISIBLE);
        this.mCbEmailSelectService.setVisibility(View.VISIBLE);
        this.mCbEmailSelectService.setChecked(false);
        this.mTvEmailSelectService.setText(getSpannableStringEmail());
        this.mTvEmailSelectService.setHighlightColor(0);
        this.mTvEmailSelectService.setMovementMethod(LinkMovementMethod.getInstance());
        this.mTvIphoneSelectService.setText(getSpannableString());
        this.mTvIphoneSelectService.setMovementMethod(LinkMovementMethod.getInstance());
        this.mTvIphoneSelectService.setHighlightColor(0);
        registerBtnIsClick(false);
        this.mTitleView.setTvTitle("");
        this.mTvTitleNmae.setText(getString(R.string.login_register_main_phone_title));
        this.mTitleView.setTvRightVisible(0);
        this.mRlIphone.setVisibility(0);
        this.mRlEmail.setVisibility(4);
        this.mEtAccount.addTextChangedListener(new EditTextWatcher(this.mEtAccount));
        this.mEtEmailAccount.addTextChangedListener(new EditTextWatcher(this.mEtEmailAccount));
        this.mEtEmailPassword.addTextChangedListener(new EditTextWatcher(this.mEtEmailPassword));
        this.mEtGetVelidationCode.addTextChangedListener(new EditTextWatcher(this.mEtGetVelidationCode));
        this.mEtIphonePassword.addTextChangedListener(new EditTextWatcher(this.mEtIphonePassword));
        this.mTvSelectCountry.setText(getResources().getString(R.string.libperson_service_china));
        FontUtil.changeViewLanTing(getAssets(), getWindow().getDecorView());
    }

    @Override
    public void doTrans() {
        OnClickListener();
    }

    private void OnClickListener() {
        this.mTitleView.setTvRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(RegisterActivity.this.mContext);
                RegisterActivity.this.mCbIphoneSelectService.setChecked(false);
                RegisterActivity.this.mCbEmailSelectService.setChecked(false);
                if (RegisterActivity.this.mRlEmail.isShown()) {
                    RegisterActivity.this.isEmail = false;
                    RegisterActivity.this.mTitleView.setTvRightText(RegisterActivity.this.getString(R.string.login_email_title));
                    RegisterActivity.this.mTvTitleNmae.setText(R.string.login_register_main_phone_title);
                    RegisterActivity.this.mBtnRegisterPhone.setVisibility(0);
                    RegisterActivity.this.mBtnRegisterEmail.setVisibility(8);
                    RegisterActivity.this.mRlEmail.setVisibility(4);
                    RegisterActivity.this.mRlIphone.setVisibility(0);
                    if (!RegisterActivity.this.isCheckPhone) {
                        RegisterActivity.this.registerBtnIsClick(false);
                        return;
                    } else if (RegisterActivity.this.mEtGetVelidationCode.getText().length() == 4) {
                        RegisterActivity.this.registerBtnIsClick(true);
                        return;
                    } else {
                        RegisterActivity.this.registerBtnIsClick(false);
                        return;
                    }
                }
                RegisterActivity.this.isEmail = true;
                RegisterActivity.this.mTitleView.setTvRightText(RegisterActivity.this.getString(R.string.login_iphone_title));
                RegisterActivity.this.mTvTitleNmae.setText(R.string.login_register_main_email_title);
                RegisterActivity.this.mRlEmail.setVisibility(0);
                RegisterActivity.this.mRlIphone.setVisibility(4);
                RegisterActivity.this.mBtnRegisterPhone.setVisibility(8);
                RegisterActivity.this.mBtnRegisterEmail.setVisibility(0);
                if (!RegisterActivity.this.isCheckEmail) {
                    RegisterActivity.this.registerBtnIsClick(false);
                } else RegisterActivity.this.registerBtnIsClick(RegisterActivity.this.mEtEmailPassword.getText().length() >= 8 && RegisterActivity.this.mEtEmailPassword.getText().length() <= 16);
            }
        });
        this.mIvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegisterActivity.this.isShowPassword) {
                    RegisterActivity.this.isShowPassword = false;
                    RegisterActivity.this.mEtEmailPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    RegisterActivity.this.mIvShowPassword.setBackgroundResource(R.drawable.iv_login_email_password);
                } else {
                    RegisterActivity.this.isShowPassword = true;
                    RegisterActivity.this.mEtEmailPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    RegisterActivity.this.mIvShowPassword.setBackgroundResource(R.drawable.iv_login_email_password_show);
                }
                RegisterActivity.this.mEtEmailPassword.requestFocus();
                RegisterActivity.this.mEtEmailPassword.setSelection(RegisterActivity.this.mEtEmailPassword.getText().length());
            }
        });
        this.mIvShowIphonePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegisterActivity.this.isShowIphonePassword) {
                    RegisterActivity.this.isShowIphonePassword = false;
                    RegisterActivity.this.mEtIphonePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    RegisterActivity.this.mIvShowIphonePassword.setBackgroundResource(R.drawable.iv_login_email_password);
                } else {
                    RegisterActivity.this.isShowIphonePassword = true;
                    RegisterActivity.this.mEtIphonePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    RegisterActivity.this.mIvShowIphonePassword.setBackgroundResource(R.drawable.iv_login_email_password_show);
                }
                RegisterActivity.this.mEtIphonePassword.requestFocus();
                RegisterActivity.this.mEtIphonePassword.setSelection(RegisterActivity.this.mEtIphonePassword.getText().length());
            }
        });
        this.mTvGetValidationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbAppUtil.closeSoftInput(RegisterActivity.this.mContext);
                RegisterActivity.this.registerPrenster.getVerificationCode(RegisterActivity.this.mEtAccount.getText().toString());
            }
        });
        this.mBtnRegisterPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(RegisterActivity.this.mContext);
                RegisterActivity.this.registerPrenster.registerByPhone(RegisterActivity.this.mEtAccount.getText().toString(), RegisterActivity.this.mEtGetVelidationCode.getText().toString(), RegisterActivity.this.mEtIphonePassword.getText().toString());
            }
        });
        this.mBtnRegisterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(RegisterActivity.this.mContext);
                RegisterActivity.this.registerPrenster.registerByEmail(RegisterActivity.this.mEtEmailAccount.getText().toString(), RegisterActivity.this.mEtEmailPassword.getText().toString(), RegisterActivity.this.mEtEmailPassword.getText().toString());
            }
        });
        this.mCbEmailSelectService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RegisterActivity.this.isCheckEmail = isChecked;
                RegisterActivity.this.judgeEmailIsClick();
            }
        });
        this.mCbIphoneSelectService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RegisterActivity.this.isCheckPhone = isChecked;
                if (!RegisterActivity.this.isCheckPhone) {
                    RegisterActivity.this.registerBtnIsClick(false);
                } else RegisterActivity.this.registerBtnIsClick(RegisterActivity.this.mEtGetVelidationCode.getText().length() == 4);
            }
        });
        this.mTvSelectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        this.mIvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        this.mEtGetVelidationCode.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtIphonePassword.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtAccount.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtEmailPassword.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtEmailAccount.setOnEditorActionListener(this.mOnEditorActionListener);
    }

    private int getEditTextCursorIndex(EditText mEditText) {
        return mEditText.getSelectionStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getCodeResult(boolean isSuccess, String errMsg) {
        if (!isSuccess) {
            this.mTvErrorHint.setVisibility(0);
            this.mTvErrorHint.setText(errMsg);
            return;
        }
        this.mTvGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
        this.mTvGetValidationCode.setEnabled(false);
        this.mTvErrorHint.setText("");
    }

    @Override
    public void updateSeconds(boolean isComplete, int seconds) {
        if (isComplete) {
            this.mTvGetValidationCode.setEnabled(true);
            this.mTvGetValidationCode.setTextColor(getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
            this.mTvGetValidationCode.setText(R.string.login_btn_verification);
            return;
        }
        this.mTvGetValidationCode.setTextColor(getResources().getColor(R.color.login_get_verfication_unclick));
        this.mTvGetValidationCode.setEnabled(false);
        this.mTvGetValidationCode.setText(seconds + getString(R.string.login_second));
    }

    @Override
    public void loginSuccess() {
        Constants.isRefreshMainView = true;
        Intent it = Router.invoke(this, "activity://app.main");
        readyGoThenKillAllActivity(it);
    }

    @Override
    public void registerIphoneResult(boolean isSuceess, String errMsg) {
        if (isSuceess) {
            Constants.isRefreshMainView = true;
            Intent it = Router.invoke(this, "activity://app.main");
            readyGoThenKillAllActivity(it);
        } else if (errMsg != null) {
            this.mTvErrorHint.setVisibility(0);
            this.mTvErrorHint.setText(errMsg);
        }
    }

    @Override
    public void registerEmailResult(boolean isSuccess, String errMsg) {
        if (isSuccess) {
            Constants.isRefreshMainView = true;
            Intent it = Router.invoke(this, "activity://app.main");
            readyGoThenKillAllActivity(it);
        } else if (errMsg != null) {
            this.mTvEmailErrorHint.setVisibility(0);
            this.mTvEmailErrorHint.setText(errMsg);
        }
    }

    public void judgeEmailIsClick() {
        registerBtnIsClick(DataValidatorUtil.isEmail(this.mEtEmailAccount.getText().toString().trim()) && this.mEtEmailPassword.getText().length() >= 8 && this.mEtEmailPassword.getText().length() <= 16 && this.isCheckEmail);
    }

    private SpannableString getSpannableString() {
        String str1 = this.mContext.getString(R.string.register_select_service1);
        String str2 = this.mContext.getString(R.string.register_select_service2);
        String str3 = this.mContext.getString(R.string.register_select_service3);
        String str4 = this.mContext.getString(R.string.register_select_service4);
        String str5 = this.mContext.getString(R.string.register_select_service5);
        SpannableString spannableString = new SpannableString(str1 + str2 + str3 + str4 + str5);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length() + str3.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length() + str3.length() + str4.length(), str1.length() + str2.length() + str3.length() + str4.length() + str5.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(RegisterActivity.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                RegisterActivity.this.goWebActivity(ComonStaticURL.getPolicyUrl(), RegisterActivity.this.getString(R.string.person_setting_user_agreement));
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(RegisterActivity.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                RegisterActivity.this.goWebActivity(ComonStaticURL.getPrivacyUrl(), RegisterActivity.this.getString(R.string.person_setting_user_privacy));
            }
        }, str1.length() + str2.length() + str3.length(), str1.length() + str2.length() + str3.length() + str4.length(), 33);
        return spannableString;
    }

    private SpannableString getSpannableStringEmail() {
        String str1 = this.mContext.getString(R.string.register_select_service1);
        String str2 = this.mContext.getString(R.string.register_select_service2);
        String str3 = this.mContext.getString(R.string.register_select_service3);
        String str4 = this.mContext.getString(R.string.register_select_service4);
        String str5 = this.mContext.getString(R.string.register_select_service_email5);
        SpannableString spannableString = new SpannableString(str1 + str2 + str3 + str4 + str5);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length(), str1.length() + str2.length() + str3.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.register_agreement)), str1.length() + str2.length() + str3.length() + str4.length(), str1.length() + str2.length() + str3.length() + str4.length() + str5.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(RegisterActivity.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                RegisterActivity.this.goWebActivity(ComonStaticURL.getPolicyUrl(), RegisterActivity.this.getString(R.string.person_setting_user_agreement));
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(RegisterActivity.this.getResources().getColor(R.color.register_agreement_click));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                RegisterActivity.this.goWebActivity(ComonStaticURL.getPrivacyUrl(), RegisterActivity.this.getString(R.string.person_setting_user_privacy));
            }
        }, str1.length() + str2.length() + str3.length(), str1.length() + str2.length() + str3.length() + str4.length(), 33);
        return spannableString;
    }

    public void goWebActivity(String url, String title) {
        Intent it = new Intent(this.mContext, UserProtocolWebViewActivity.class);
        it.putExtra("web_url", url);
        it.putExtra("web_title", title);
        startActivity(it);
        overridePendingTransition(17432576, 17432577);
    }

    public void registerBtnIsClick(boolean isClick) {
        if (isClick) {
            this.mBtnRegisterPhone.setEnabled(true);
            this.mBtnRegisterEmail.setEnabled(true);
            return;
        }
        this.mBtnRegisterPhone.setEnabled(false);
        this.mBtnRegisterEmail.setEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 121) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public class EditTextWatcher implements TextWatcher {
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
                if (R.id.et_account == this.mEditText.getId()) {
                    RegisterActivity.this.mTvErrorHint.setText("");
                } else if (R.id.et_email_password == this.mEditText.getId()) {
                    RegisterActivity.this.judgeEmailIsClick();
                    RegisterActivity.this.mTvEmailErrorHint.setText("");
                } else if (R.id.et_email_account == this.mEditText.getId()) {
                    RegisterActivity.this.judgeEmailIsClick();
                    RegisterActivity.this.mTvEmailErrorHint.setText("");
                }
            } else if (R.id.et_account == this.mEditText.getId()) {
                RegisterActivity.this.mTvErrorHint.setText("");
            } else if (R.id.et_email_password == this.mEditText.getId()) {
                RegisterActivity.this.judgeEmailIsClick();
                RegisterActivity.this.mTvEmailErrorHint.setText("");
            } else if (R.id.et_email_account == this.mEditText.getId()) {
                RegisterActivity.this.judgeEmailIsClick();
                RegisterActivity.this.mTvEmailErrorHint.setText("");
            }
            if (R.id.et_password == this.mEditText.getId()) {
                RegisterActivity.this.mTvErrorHint.setText("");
            }
            if (R.id.et_account == this.mEditText.getId()) {
                if (!DataValidatorUtil.isMobile(s.toString().trim())) {
                    RegisterActivity.this.mTvGetValidationCode.setTextColor(RegisterActivity.this.getResources().getColor(R.color.login_get_verfication_unclick));
                    RegisterActivity.this.mTvGetValidationCode.setEnabled(false);
                } else {
                    RegisterActivity.this.mTvGetValidationCode.setEnabled(true);
                    RegisterActivity.this.mTvGetValidationCode.setTextColor(RegisterActivity.this.getResources().getColorStateList(R.color.selector_btn_register_get_verfication_code));
                }
            }
            if (R.id.et_password == this.mEditText.getId()) {
                if (!RegisterActivity.this.isCheckPhone) {
                    RegisterActivity.this.registerBtnIsClick(false);
                } else RegisterActivity.this.registerBtnIsClick(s.length() >= 8 && RegisterActivity.this.mEtGetVelidationCode.getText().length() == 4);
            }
            if (R.id.et_verification == this.mEditText.getId()) {
                if (!RegisterActivity.this.isCheckPhone) {
                    RegisterActivity.this.registerBtnIsClick(false);
                } else RegisterActivity.this.registerBtnIsClick(s.length() == 4 && RegisterActivity.this.mEtIphonePassword.getText().length() >= 8);
            }
        }
    }
}

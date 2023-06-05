package com.fimi.libperson.ui.me.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.fimi.android.app.R;
import com.fimi.host.HostConstants;
import com.fimi.kernel.Constants;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.ivew.ILoginView;
import com.fimi.libperson.presenter.LoginPresenter;
import com.fimi.libperson.widget.TitleView;

import router.Router;

/* loaded from: classes.dex */
public class LoginMainActivity extends BasePersonActivity implements ILoginView, ForgetPasswordFragment.OnResetPasswordListerner, ForgetIphonePasswordFragment.OnResetIphonePasswordListerner {
    LoginPresenter loginPresenter;
    Button mBtnLoginEmail;
    Button mBtnLoginIphone;
    CheckBox mCbSelectService;
    EditText mEtAccount;
    EditText mEtEmailAccount;
    EditText mEtEmailPassword;
    EditText mEtPassword;
    ImageView mIvReturn;
    ImageView mIvShowIphonePassword;
    ImageView mIvShowPassword;
    RelativeLayout mRlEmail;
    RelativeLayout mRlIphone;
    TitleView mTitleView;
    TextView mTvAreaCode;
    TextView mTvEmailErrorHint;
    TextView mTvErrorHint;
    TextView mTvForgetHint;
    TextView mTvForgetIphoneHint;
    TextView mTvSelectCountry;
    TextView mTvTitleNmae;
    boolean isEmail = false;
    private FragmentManager fragmentManager;
    private ForgetIphonePasswordFragment mForgetIphonePasswordFragment;
    private ForgetPasswordFragment mForgetPasswordFragment;
    private boolean isShowPassword = false;
    private boolean isShowIphonePassword = false;
    private boolean isMainLand = true;
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.10
        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 4 || actionId == 6 || (event != null && 66 == event.getKeyCode() && event.getAction() == 0)) {
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
                return false;
            }
            return false;
        }
    };

    @Override // com.fimi.libperson.BasePersonActivity, com.fimi.kernel.base.BaseActivity
    public void setStatusBarColor() {
        super.setStatusBarColor();
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.activity_login_main;
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        initView();
        String isEmailStr = SPStoreManager.getInstance().getString(HostConstants.SP_KEY_USER_INFO_FLAG);
        ServiceItem serviceItem = (ServiceItem) SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class);
        if (serviceItem == null || serviceItem.getInfo() == R.string.region_Mainland_China) {
            this.isMainLand = true;
            if (isEmailStr != null) {
                if (isEmailStr.equals("1")) {
                    showEmail(true);
                    this.mEtEmailAccount.setText(SPStoreManager.getInstance().getString(HostConstants.SP_KEY_USER_INFO_EMAIL_OR_IPHONE));
                    this.mEtEmailAccount.setSelection(this.mEtEmailAccount.getText().length());
                    return;
                } else if (isEmailStr.equals("0")) {
                    showEmail(false);
                    this.mEtAccount.setText(SPStoreManager.getInstance().getString(HostConstants.SP_KEY_USER_INFO_EMAIL_OR_IPHONE));
                    this.mEtAccount.setSelection(this.mEtAccount.getText().length());
                    return;
                } else {
                    return;
                }
            }
            return;
        }
        this.isMainLand = false;
        showEmail(true);
        this.mEtAccount.setText(SPStoreManager.getInstance().getString(HostConstants.SP_KEY_USER_INFO_EMAIL_OR_IPHONE));
        this.mEtAccount.setSelection(this.mEtAccount.getText().length());
        this.mTitleView.setRightTvIsVisible(false);
    }

    private void initView() {
        this.mIvReturn = (ImageView) findViewById(R.id.iv_return);
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mTvSelectCountry = (TextView) findViewById(R.id.tv_select_country);
        this.mTvAreaCode = (TextView) findViewById(R.id.tv_area_code);
        this.mEtAccount = (EditText) findViewById(R.id.et_account);
        this.mEtPassword = (EditText) findViewById(R.id.et_password);
        this.mTvErrorHint = (TextView) findViewById(R.id.tv_error_hint);
        this.mCbSelectService = (CheckBox) findViewById(R.id.cb_iphone_select_service);
        this.mEtEmailAccount = (EditText) findViewById(R.id.et_email_account);
        this.mEtEmailPassword = (EditText) findViewById(R.id.et_email_password);
        this.mTvEmailErrorHint = (TextView) findViewById(R.id.tv_email_error_hint);
        this.mTvForgetHint = (TextView) findViewById(R.id.tv_forget_hint);
        this.mTvForgetIphoneHint = (TextView) findViewById(R.id.tv_forget_iphone_hint);
        this.mBtnLoginIphone = (Button) findViewById(R.id.btn_login_phone);
        this.mBtnLoginEmail = (Button) findViewById(R.id.btn_login_email);
        this.mRlIphone = (RelativeLayout) findViewById(R.id.rl_iphone);
        this.mRlEmail = (RelativeLayout) findViewById(R.id.rl_email);
        this.mIvShowPassword = (ImageView) findViewById(R.id.iv_show_password);
        this.mIvShowIphonePassword = (ImageView) findViewById(R.id.iv_show_iphone_password);
        this.mTvTitleNmae = (TextView) findViewById(R.id.tv_title_name);
        loginBtnIsClick(false);
        this.mTvTitleNmae.setText(getString(R.string.login_login_main_phone_title));
        this.mTitleView.setTvRightVisible(0);
        this.mTitleView.setTvTitle("");
        this.mRlIphone.setVisibility(0);
        this.mRlEmail.setVisibility(8);
        this.loginPresenter = new LoginPresenter(this);
        this.mEtAccount.addTextChangedListener(new EditTextWatcher(this.mEtAccount));
        this.mEtEmailAccount.addTextChangedListener(new EditTextWatcher(this.mEtEmailAccount));
        this.mEtEmailPassword.addTextChangedListener(new EditTextWatcher(this.mEtEmailPassword));
        this.mEtPassword.addTextChangedListener(new EditTextWatcher(this.mEtPassword));
        this.mTvSelectCountry.setText(getResources().getString(R.string.libperson_service_china));
        FontUtil.changeViewLanTing(getAssets(), getWindow().getDecorView());
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
        OnClickListerner();
        this.fragmentManager = getSupportFragmentManager();
        this.mForgetPasswordFragment = (ForgetPasswordFragment) this.fragmentManager.findFragmentById(R.id.fl_fp);
        if (this.mForgetPasswordFragment == null) {
            this.mForgetPasswordFragment = new ForgetPasswordFragment();
            this.fragmentManager.beginTransaction().add(R.id.fl_fp, this.mForgetPasswordFragment, "forgetPassword").hide(this.mForgetPasswordFragment).commitAllowingStateLoss();
        } else {
            this.fragmentManager.beginTransaction().hide(this.mForgetPasswordFragment).commit();
        }
        this.mForgetIphonePasswordFragment = (ForgetIphonePasswordFragment) this.fragmentManager.findFragmentById(R.id.fl_fp_iphone);
        if (this.mForgetIphonePasswordFragment == null) {
            this.mForgetIphonePasswordFragment = new ForgetIphonePasswordFragment();
            this.fragmentManager.beginTransaction().add(R.id.fl_fp_iphone, this.mForgetIphonePasswordFragment, "forget_iphone_password_fragment").hide(this.mForgetIphonePasswordFragment).commitAllowingStateLoss();
            return;
        }
        this.fragmentManager.beginTransaction().hide(this.mForgetIphonePasswordFragment).commit();
    }

    private void OnClickListerner() {
        this.mTitleView.setTvRightListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
                if (LoginMainActivity.this.mRlEmail.isShown()) {
                    LoginMainActivity.this.showEmail(false);
                } else {
                    LoginMainActivity.this.showEmail(true);
                }
            }
        });
        this.mIvShowPassword.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (LoginMainActivity.this.isShowPassword) {
                    LoginMainActivity.this.isShowPassword = false;
                    LoginMainActivity.this.mEtEmailPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    LoginMainActivity.this.mIvShowPassword.setBackgroundResource(R.drawable.iv_login_email_password);
                } else {
                    LoginMainActivity.this.isShowPassword = true;
                    LoginMainActivity.this.mEtEmailPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    LoginMainActivity.this.mIvShowPassword.setBackgroundResource(R.drawable.iv_login_email_password_show);
                }
                LoginMainActivity.this.mEtEmailPassword.requestFocus();
                LoginMainActivity.this.mEtEmailPassword.setSelection(LoginMainActivity.this.mEtEmailPassword.getText().length());
            }
        });
        this.mIvShowIphonePassword.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (LoginMainActivity.this.isShowIphonePassword) {
                    LoginMainActivity.this.isShowIphonePassword = false;
                    LoginMainActivity.this.mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    LoginMainActivity.this.mIvShowIphonePassword.setBackgroundResource(R.drawable.iv_login_email_password);
                } else {
                    LoginMainActivity.this.isShowIphonePassword = true;
                    LoginMainActivity.this.mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    LoginMainActivity.this.mIvShowIphonePassword.setBackgroundResource(R.drawable.iv_login_email_password_show);
                }
                LoginMainActivity.this.mEtPassword.requestFocus();
                LoginMainActivity.this.mEtPassword.setSelection(LoginMainActivity.this.mEtPassword.getText().length());
            }
        });
        this.mBtnLoginIphone.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
                LoginMainActivity.this.loginPresenter.loginByPhone(LoginMainActivity.this.mEtAccount.getText().toString(), LoginMainActivity.this.mEtPassword.getText().toString());
            }
        });
        this.mBtnLoginEmail.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
                LoginMainActivity.this.loginPresenter.loginByEmail(LoginMainActivity.this.mEtEmailAccount.getText().toString(), LoginMainActivity.this.mEtEmailPassword.getText().toString());
            }
        });
        this.mTvForgetHint.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LoginMainActivity.this.fragmentManager.beginTransaction().show(LoginMainActivity.this.mForgetPasswordFragment).commitAllowingStateLoss();
                LoginMainActivity.this.mForgetPasswordFragment.setEmailAddress(LoginMainActivity.this.mEtEmailAccount.getText().toString().trim());
                LoginMainActivity.this.mTitleView.setTvRightVisible(4);
                LoginMainActivity.this.mBtnLoginEmail.setVisibility(4);
                LoginMainActivity.this.mBtnLoginIphone.setVisibility(4);
                LoginMainActivity.this.mEtEmailPassword.setVisibility(4);
                LoginMainActivity.this.mEtEmailAccount.setVisibility(4);
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
            }
        });
        this.mTvForgetIphoneHint.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LoginMainActivity.this.fragmentManager.beginTransaction().show(LoginMainActivity.this.mForgetIphonePasswordFragment).commitAllowingStateLoss();
                LoginMainActivity.this.mForgetIphonePasswordFragment.setIphone(LoginMainActivity.this.mEtAccount.getText().toString().trim());
                LoginMainActivity.this.mTitleView.setTvRightVisible(4);
                LoginMainActivity.this.mBtnLoginEmail.setVisibility(4);
                LoginMainActivity.this.mBtnLoginIphone.setVisibility(4);
                LoginMainActivity.this.mEtAccount.setVisibility(4);
                LoginMainActivity.this.mEtPassword.setVisibility(4);
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
            }
        });
        this.mIvReturn.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (LoginMainActivity.this.mForgetPasswordFragment == null || !LoginMainActivity.this.mForgetPasswordFragment.isVisible()) {
                    if (LoginMainActivity.this.mForgetIphonePasswordFragment != null && LoginMainActivity.this.mForgetIphonePasswordFragment.isVisible()) {
                        if (LoginMainActivity.this.mForgetIphonePasswordFragment.getState() == ForgetIphonePasswordFragment.State.IPHONE) {
                            LoginMainActivity.this.mForgetIphonePasswordFragment.setBack();
                            LoginMainActivity.this.fragmentManager.beginTransaction().hide(LoginMainActivity.this.mForgetIphonePasswordFragment).commit();
                            LoginMainActivity.this.mTvTitleNmae.setText(LoginMainActivity.this.getString(R.string.login_login_main_phone_title));
                            LoginMainActivity.this.mTitleView.setTvRightVisible(0);
                            LoginMainActivity.this.mBtnLoginIphone.setVisibility(0);
                            LoginMainActivity.this.mEtAccount.setVisibility(0);
                            LoginMainActivity.this.mEtPassword.setVisibility(0);
                            return;
                        }
                        LoginMainActivity.this.mForgetIphonePasswordFragment.setBack();
                        return;
                    }
                    LoginMainActivity.this.finish();
                } else if (LoginMainActivity.this.mForgetPasswordFragment.getState() == ForgetPasswordFragment.State.EMAIL) {
                    LoginMainActivity.this.mForgetPasswordFragment.setBack();
                    LoginMainActivity.this.fragmentManager.beginTransaction().hide(LoginMainActivity.this.mForgetPasswordFragment).commit();
                    LoginMainActivity.this.mTvTitleNmae.setText(LoginMainActivity.this.getString(R.string.login_login_main_email_title));
                    if (LoginMainActivity.this.isMainLand) {
                        LoginMainActivity.this.mTitleView.setTvRightVisible(0);
                    }
                    LoginMainActivity.this.mEtEmailPassword.setVisibility(0);
                    LoginMainActivity.this.mEtEmailAccount.setVisibility(0);
                    LoginMainActivity.this.mBtnLoginEmail.setVisibility(0);
                } else {
                    LoginMainActivity.this.mForgetPasswordFragment.setBack();
                }
            }
        });
        this.mTvSelectCountry.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.me.login.LoginMainActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
            }
        });
        this.mEtPassword.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtAccount.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtEmailAccount.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtEmailPassword.setOnEditorActionListener(this.mOnEditorActionListener);
    }

    public void judgeEmailIsClick() {
        if (DataValidatorUtil.isEmail(this.mEtEmailAccount.getText().toString().trim()) && this.mEtEmailPassword.getText().length() >= 8 && this.mEtEmailPassword.getText().length() <= 16) {
            loginBtnIsClick(true);
        } else {
            loginBtnIsClick(false);
        }
    }

    @Override
    // android.support.v7.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.mForgetPasswordFragment != null && this.mForgetPasswordFragment.isVisible()) {
                if (this.mForgetPasswordFragment.getState() == ForgetPasswordFragment.State.EMAIL) {
                    this.mForgetPasswordFragment.setBack();
                    this.fragmentManager.beginTransaction().hide(this.mForgetPasswordFragment).commit();
                    this.mTvTitleNmae.setText(getString(R.string.login_login_main_email_title));
                    if (this.isMainLand) {
                        this.mTitleView.setTvRightVisible(0);
                    }
                    this.mBtnLoginEmail.setVisibility(0);
                    this.mEtEmailPassword.setVisibility(0);
                    this.mEtEmailAccount.setVisibility(0);
                    return false;
                }
                this.mForgetPasswordFragment.setBack();
                return false;
            } else if (this.mForgetIphonePasswordFragment != null && this.mForgetIphonePasswordFragment.isVisible()) {
                if (this.mForgetIphonePasswordFragment.getState() == ForgetIphonePasswordFragment.State.IPHONE) {
                    this.mForgetIphonePasswordFragment.setBack();
                    this.fragmentManager.beginTransaction().hide(this.mForgetIphonePasswordFragment).commit();
                    this.mTvTitleNmae.setText(getString(R.string.login_login_main_phone_title));
                    this.mTitleView.setTvRightVisible(0);
                    this.mBtnLoginIphone.setVisibility(0);
                    this.mEtAccount.setVisibility(0);
                    this.mEtPassword.setVisibility(0);
                    return false;
                }
                this.mForgetIphonePasswordFragment.setBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private int getEditTextCursorIndex(EditText mEditText) {
        return mEditText.getSelectionStart();
    }

    @Override // com.fimi.libperson.ui.me.login.ForgetPasswordFragment.OnResetPasswordListerner
    public void resetSuccess() {
        this.fragmentManager.beginTransaction().hide(this.mForgetPasswordFragment).commit();
        this.mForgetPasswordFragment.setState(ForgetPasswordFragment.State.EMAIL);
        this.mTvTitleNmae.setText(getString(R.string.login_login_main_email_title));
        if (this.isMainLand) {
            this.mTitleView.setTvRightVisible(0);
        }
        this.mBtnLoginEmail.setVisibility(0);
        this.mEtEmailPassword.setVisibility(0);
        this.mEtEmailAccount.setVisibility(0);
    }

    @Override
    // com.fimi.libperson.ui.me.login.ForgetIphonePasswordFragment.OnResetIphonePasswordListerner
    public void resetIphoneSuccess() {
        this.fragmentManager.beginTransaction().hide(this.mForgetIphonePasswordFragment).commit();
        this.mForgetIphonePasswordFragment.setState(ForgetIphonePasswordFragment.State.IPHONE);
        this.mTvTitleNmae.setText(getString(R.string.login_login_main_phone_title));
        this.mTitleView.setTvRightVisible(0);
        this.mBtnLoginIphone.setVisibility(0);
        this.mEtAccount.setVisibility(0);
        this.mEtPassword.setVisibility(0);
    }

    @Override // com.fimi.libperson.ivew.ILoginView
    public void getCodeResult(boolean isSuccess, String errMsg) {
        if (!isSuccess) {
            this.mTvErrorHint.setVisibility(0);
            this.mTvErrorHint.setText(errMsg);
        }
    }

    @Override // com.fimi.libperson.ivew.ILoginView
    public void iphoneLoginResult(boolean isSuccess, String errMsg) {
        if (isSuccess) {
            Intent it = (Intent) Router.invoke(this, "activity://app.main");
            readyGoThenKillAllActivity(it);
        } else if (errMsg != null) {
            this.mTvErrorHint.setVisibility(0);
            this.mTvErrorHint.setText(errMsg);
        }
    }

    @Override // com.fimi.libperson.ivew.ILoginView
    public void emailLoginResult(boolean isSuccess, String errMsg) {
        if (isSuccess) {
            Constants.isRefreshMainView = true;
            Intent it = (Intent) Router.invoke(this, "activity://app.main");
            readyGoThenKillAllActivity(it);
        } else if (errMsg != null) {
            this.mTvEmailErrorHint.setVisibility(0);
            this.mTvEmailErrorHint.setText(errMsg);
        }
    }

    @Override // com.fimi.libperson.ivew.ILoginView
    public void updateSeconds(boolean isComplete, int seconds) {
    }

    @Override // com.fimi.libperson.ivew.ILoginView
    public void freorgottenPasswords(boolean isFrequently) {
        if (isFrequently) {
            this.mTvForgetHint.setTextColor(getResources().getColor(R.color.login_forget_password_frequently));
        } else {
            this.mTvForgetHint.setTextColor(getResources().getColor(R.color.login_forget_password));
        }
    }

    @Override // com.fimi.libperson.ivew.ILoginView
    public void loginSuccess() {
        Constants.isRefreshMainView = true;
        Intent it = (Intent) Router.invoke(this, "activity://app.main");
        readyGoThenKillAllActivity(it);
    }

    public void loginBtnIsClick(boolean isClick) {
        if (isClick) {
            this.mBtnLoginIphone.setEnabled(true);
            this.mBtnLoginEmail.setEnabled(true);
            return;
        }
        this.mBtnLoginIphone.setEnabled(false);
        this.mBtnLoginEmail.setEnabled(false);
    }

    public void showEmail(boolean isEmail) {
        if (isEmail) {
            this.isEmail = true;
            this.mTvForgetHint.setVisibility(0);
            this.mTvForgetIphoneHint.setVisibility(8);
            this.mTitleView.setTvRightText(getString(R.string.login_iphone_title));
            this.mTvTitleNmae.setText(getString(R.string.login_login_main_email_title));
            this.mBtnLoginEmail.setVisibility(0);
            this.mBtnLoginIphone.setVisibility(4);
            this.mRlEmail.setVisibility(0);
            this.mRlIphone.setVisibility(8);
            if (this.mEtEmailPassword.getText().length() >= 8 && this.mEtEmailPassword.getText().length() <= 16) {
                loginBtnIsClick(true);
                return;
            } else {
                loginBtnIsClick(false);
                return;
            }
        }
        this.isEmail = false;
        this.mTitleView.setTvRightText(getString(R.string.login_email_title));
        this.mTvTitleNmae.setText(getString(R.string.login_login_main_phone_title));
        this.mBtnLoginEmail.setVisibility(4);
        this.mBtnLoginIphone.setVisibility(0);
        this.mRlEmail.setVisibility(8);
        this.mRlIphone.setVisibility(0);
        this.mTvForgetHint.setVisibility(8);
        this.mTvForgetIphoneHint.setVisibility(0);
        this.mTvForgetHint.setTextColor(getResources().getColor(R.color.login_forget_password));
        if (this.mEtPassword.getText().length() == 4) {
            loginBtnIsClick(true);
        } else {
            loginBtnIsClick(false);
        }
    }

    /* loaded from: classes.dex */
    public class EditTextWatcher implements TextWatcher {
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
                if (R.id.et_account == this.mEditText.getId()) {
                    LoginMainActivity.this.mTvErrorHint.setText("");
                } else if (R.id.et_email_password == this.mEditText.getId()) {
                    LoginMainActivity.this.judgeEmailIsClick();
                    LoginMainActivity.this.mTvEmailErrorHint.setText("");
                } else if (R.id.et_email_account == this.mEditText.getId()) {
                    LoginMainActivity.this.judgeEmailIsClick();
                    LoginMainActivity.this.mTvEmailErrorHint.setText("");
                }
            } else if (R.id.et_account == this.mEditText.getId()) {
                LoginMainActivity.this.mTvErrorHint.setText("");
            } else if (R.id.et_email_password == this.mEditText.getId()) {
                LoginMainActivity.this.judgeEmailIsClick();
                LoginMainActivity.this.mTvEmailErrorHint.setText("");
            } else if (R.id.et_email_account == this.mEditText.getId()) {
                LoginMainActivity.this.judgeEmailIsClick();
                LoginMainActivity.this.mTvEmailErrorHint.setText("");
            }
            if (R.id.et_password == this.mEditText.getId()) {
                LoginMainActivity.this.mTvErrorHint.setText("");
            }
            if (R.id.et_account == this.mEditText.getId()) {
                if (!DataValidatorUtil.isMobile(s.toString().trim()) || LoginMainActivity.this.mEtPassword.getText().length() < 8) {
                    LoginMainActivity.this.loginBtnIsClick(false);
                } else {
                    LoginMainActivity.this.loginBtnIsClick(true);
                }
            }
            if (R.id.et_password == this.mEditText.getId()) {
                if (s.length() < 8 || !DataValidatorUtil.isMobile(LoginMainActivity.this.mEtAccount.getText().toString().trim())) {
                    LoginMainActivity.this.loginBtnIsClick(false);
                } else {
                    LoginMainActivity.this.loginBtnIsClick(true);
                }
            }
        }
    }
}

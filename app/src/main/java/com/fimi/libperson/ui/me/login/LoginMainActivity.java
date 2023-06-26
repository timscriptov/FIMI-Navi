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


public class LoginMainActivity extends BasePersonActivity implements ILoginView, ForgetPasswordFragment.OnResetPasswordListerner, ForgetIphonePasswordFragment.OnResetIphonePasswordListerner {
    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 4 || actionId == 6 || (event != null && 66 == event.getKeyCode() && event.getAction() == 0)) {
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
                return false;
            }
            return false;
        }
    };
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

    @Override
    public void setStatusBarColor() {
        super.setStatusBarColor();
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login_main;
    }

    @Override
    public void initData() {
        initView();
        String isEmailStr = SPStoreManager.getInstance().getString(HostConstants.SP_KEY_USER_INFO_FLAG);
        ServiceItem serviceItem = SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class);
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
        this.mIvReturn = findViewById(R.id.iv_return);
        this.mTitleView = findViewById(R.id.title_view);
        this.mTvSelectCountry = findViewById(R.id.tv_select_country);
        this.mTvAreaCode = findViewById(R.id.tv_area_code);
        this.mEtAccount = findViewById(R.id.et_account);
        this.mEtPassword = findViewById(R.id.et_password);
        this.mTvErrorHint = findViewById(R.id.tv_error_hint);
        this.mCbSelectService = findViewById(R.id.cb_iphone_select_service);
        this.mEtEmailAccount = findViewById(R.id.et_email_account);
        this.mEtEmailPassword = findViewById(R.id.et_email_password);
        this.mTvEmailErrorHint = findViewById(R.id.tv_email_error_hint);
        this.mTvForgetHint = findViewById(R.id.tv_forget_hint);
        this.mTvForgetIphoneHint = findViewById(R.id.tv_forget_iphone_hint);
        this.mBtnLoginIphone = findViewById(R.id.btn_login_phone);
        this.mBtnLoginEmail = findViewById(R.id.btn_login_email);
        this.mRlIphone = findViewById(R.id.rl_iphone);
        this.mRlEmail = findViewById(R.id.rl_email);
        this.mIvShowPassword = findViewById(R.id.iv_show_password);
        this.mIvShowIphonePassword = findViewById(R.id.iv_show_iphone_password);
        this.mTvTitleNmae = findViewById(R.id.tv_title_name);
        loginBtnIsClick(false);
        this.mTvTitleNmae.setText(getString(R.string.login_login_main_phone_title));
        this.mTitleView.setTvRightVisible(0);
        this.mTitleView.setTvTitle("");
        this.mRlIphone.setVisibility(View.VISIBLE);
        this.mRlEmail.setVisibility(View.GONE);
        this.loginPresenter = new LoginPresenter(this);
        this.mEtAccount.addTextChangedListener(new EditTextWatcher(this.mEtAccount));
        this.mEtEmailAccount.addTextChangedListener(new EditTextWatcher(this.mEtEmailAccount));
        this.mEtEmailPassword.addTextChangedListener(new EditTextWatcher(this.mEtEmailPassword));
        this.mEtPassword.addTextChangedListener(new EditTextWatcher(this.mEtPassword));
        this.mTvSelectCountry.setText(getResources().getString(R.string.libperson_service_china));
        FontUtil.changeViewLanTing(getAssets(), getWindow().getDecorView());
    }

    @Override
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
        this.mTitleView.setTvRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
                LoginMainActivity.this.showEmail(!LoginMainActivity.this.mRlEmail.isShown());
            }
        });
        this.mIvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
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
        this.mIvShowIphonePassword.setOnClickListener(new View.OnClickListener() {
            @Override
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
        this.mBtnLoginIphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
                LoginMainActivity.this.loginPresenter.loginByPhone(LoginMainActivity.this.mEtAccount.getText().toString(), LoginMainActivity.this.mEtPassword.getText().toString());
            }
        });
        this.mBtnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
                LoginMainActivity.this.loginPresenter.loginByEmail(LoginMainActivity.this.mEtEmailAccount.getText().toString(), LoginMainActivity.this.mEtEmailPassword.getText().toString());
            }
        });
        this.mTvForgetHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginMainActivity.this.fragmentManager.beginTransaction().show(LoginMainActivity.this.mForgetPasswordFragment).commitAllowingStateLoss();
                LoginMainActivity.this.mForgetPasswordFragment.setEmailAddress(LoginMainActivity.this.mEtEmailAccount.getText().toString().trim());
                LoginMainActivity.this.mTitleView.setTvRightVisible(4);
                LoginMainActivity.this.mBtnLoginEmail.setVisibility(View.INVISIBLE);
                LoginMainActivity.this.mBtnLoginIphone.setVisibility(View.INVISIBLE);
                LoginMainActivity.this.mEtEmailPassword.setVisibility(View.INVISIBLE);
                LoginMainActivity.this.mEtEmailAccount.setVisibility(View.INVISIBLE);
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
            }
        });
        this.mTvForgetIphoneHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginMainActivity.this.fragmentManager.beginTransaction().show(LoginMainActivity.this.mForgetIphonePasswordFragment).commitAllowingStateLoss();
                LoginMainActivity.this.mForgetIphonePasswordFragment.setIphone(LoginMainActivity.this.mEtAccount.getText().toString().trim());
                LoginMainActivity.this.mTitleView.setTvRightVisible(4);
                LoginMainActivity.this.mBtnLoginEmail.setVisibility(View.INVISIBLE);
                LoginMainActivity.this.mBtnLoginIphone.setVisibility(View.INVISIBLE);
                LoginMainActivity.this.mEtAccount.setVisibility(View.INVISIBLE);
                LoginMainActivity.this.mEtPassword.setVisibility(View.INVISIBLE);
                AbAppUtil.closeSoftInput(LoginMainActivity.this.mContext);
            }
        });
        this.mIvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginMainActivity.this.mForgetPasswordFragment == null || !LoginMainActivity.this.mForgetPasswordFragment.isVisible()) {
                    if (LoginMainActivity.this.mForgetIphonePasswordFragment != null && LoginMainActivity.this.mForgetIphonePasswordFragment.isVisible()) {
                        if (LoginMainActivity.this.mForgetIphonePasswordFragment.getState() == ForgetIphonePasswordFragment.State.IPHONE) {
                            LoginMainActivity.this.mForgetIphonePasswordFragment.setBack();
                            LoginMainActivity.this.fragmentManager.beginTransaction().hide(LoginMainActivity.this.mForgetIphonePasswordFragment).commit();
                            LoginMainActivity.this.mTvTitleNmae.setText(LoginMainActivity.this.getString(R.string.login_login_main_phone_title));
                            LoginMainActivity.this.mTitleView.setTvRightVisible(0);
                            LoginMainActivity.this.mBtnLoginIphone.setVisibility(View.VISIBLE);
                            LoginMainActivity.this.mEtAccount.setVisibility(View.VISIBLE);
                            LoginMainActivity.this.mEtPassword.setVisibility(View.VISIBLE);
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
                    LoginMainActivity.this.mEtEmailPassword.setVisibility(View.VISIBLE);
                    LoginMainActivity.this.mEtEmailAccount.setVisibility(View.VISIBLE);
                    LoginMainActivity.this.mBtnLoginEmail.setVisibility(View.VISIBLE);
                } else {
                    LoginMainActivity.this.mForgetPasswordFragment.setBack();
                }
            }
        });
        this.mTvSelectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        this.mEtPassword.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtAccount.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtEmailAccount.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mEtEmailPassword.setOnEditorActionListener(this.mOnEditorActionListener);
    }

    public void judgeEmailIsClick() {
        loginBtnIsClick(DataValidatorUtil.isEmail(this.mEtEmailAccount.getText().toString().trim()) && this.mEtEmailPassword.getText().length() >= 8 && this.mEtEmailPassword.getText().length() <= 16);
    }

    @Override
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
                    this.mBtnLoginEmail.setVisibility(View.VISIBLE);
                    this.mEtEmailPassword.setVisibility(View.VISIBLE);
                    this.mEtEmailAccount.setVisibility(View.VISIBLE);
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
                    this.mBtnLoginIphone.setVisibility(View.VISIBLE);
                    this.mEtAccount.setVisibility(View.VISIBLE);
                    this.mEtPassword.setVisibility(View.VISIBLE);
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

    @Override
    public void resetSuccess() {
        this.fragmentManager.beginTransaction().hide(this.mForgetPasswordFragment).commit();
        this.mForgetPasswordFragment.setState(ForgetPasswordFragment.State.EMAIL);
        this.mTvTitleNmae.setText(getString(R.string.login_login_main_email_title));
        if (this.isMainLand) {
            this.mTitleView.setTvRightVisible(0);
        }
        this.mBtnLoginEmail.setVisibility(View.VISIBLE);
        this.mEtEmailPassword.setVisibility(View.VISIBLE);
        this.mEtEmailAccount.setVisibility(View.VISIBLE);
    }

    @Override
    public void resetIphoneSuccess() {
        this.fragmentManager.beginTransaction().hide(this.mForgetIphonePasswordFragment).commit();
        this.mForgetIphonePasswordFragment.setState(ForgetIphonePasswordFragment.State.IPHONE);
        this.mTvTitleNmae.setText(getString(R.string.login_login_main_phone_title));
        this.mTitleView.setTvRightVisible(0);
        this.mBtnLoginIphone.setVisibility(View.VISIBLE);
        this.mEtAccount.setVisibility(View.VISIBLE);
        this.mEtPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void getCodeResult(boolean isSuccess, String errMsg) {
        if (!isSuccess) {
            this.mTvErrorHint.setVisibility(View.VISIBLE);
            this.mTvErrorHint.setText(errMsg);
        }
    }

    @Override
    public void iphoneLoginResult(boolean isSuccess, String errMsg) {
        if (isSuccess) {
            Intent it = Router.invoke(this, "activity://app.main");
            readyGoThenKillAllActivity(it);
        } else if (errMsg != null) {
            this.mTvErrorHint.setVisibility(View.VISIBLE);
            this.mTvErrorHint.setText(errMsg);
        }
    }

    @Override
    public void emailLoginResult(boolean isSuccess, String errMsg) {
        if (isSuccess) {
            Constants.isRefreshMainView = true;
            Intent it = Router.invoke(this, "activity://app.main");
            readyGoThenKillAllActivity(it);
        } else if (errMsg != null) {
            this.mTvEmailErrorHint.setVisibility(View.VISIBLE);
            this.mTvEmailErrorHint.setText(errMsg);
        }
    }

    @Override
    public void updateSeconds(boolean isComplete, int seconds) {
    }

    @Override
    public void freorgottenPasswords(boolean isFrequently) {
        if (isFrequently) {
            this.mTvForgetHint.setTextColor(getResources().getColor(R.color.login_forget_password_frequently));
        } else {
            this.mTvForgetHint.setTextColor(getResources().getColor(R.color.login_forget_password));
        }
    }

    @Override
    public void loginSuccess() {
        Constants.isRefreshMainView = true;
        Intent it = Router.invoke(this, "activity://app.main");
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
            this.mTvForgetHint.setVisibility(View.VISIBLE);
            this.mTvForgetIphoneHint.setVisibility(View.GONE);
            this.mTitleView.setTvRightText(getString(R.string.login_iphone_title));
            this.mTvTitleNmae.setText(getString(R.string.login_login_main_email_title));
            this.mBtnLoginEmail.setVisibility(View.VISIBLE);
            this.mBtnLoginIphone.setVisibility(View.INVISIBLE);
            this.mRlEmail.setVisibility(View.VISIBLE);
            this.mRlIphone.setVisibility(View.GONE);
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
        this.mBtnLoginEmail.setVisibility(View.INVISIBLE);
        this.mBtnLoginIphone.setVisibility(View.VISIBLE);
        this.mRlEmail.setVisibility(View.GONE);
        this.mRlIphone.setVisibility(View.VISIBLE);
        this.mTvForgetHint.setVisibility(View.GONE);
        this.mTvForgetIphoneHint.setVisibility(View.VISIBLE);
        this.mTvForgetHint.setTextColor(getResources().getColor(R.color.login_forget_password));
        loginBtnIsClick(this.mEtPassword.getText().length() == 4);
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
                LoginMainActivity.this.loginBtnIsClick(DataValidatorUtil.isMobile(s.toString().trim()) && LoginMainActivity.this.mEtPassword.getText().length() >= 8);
            }
            if (R.id.et_password == this.mEditText.getId()) {
                LoginMainActivity.this.loginBtnIsClick(s.length() >= 8 && DataValidatorUtil.isMobile(LoginMainActivity.this.mEtAccount.getText().toString().trim()));
            }
        }
    }
}

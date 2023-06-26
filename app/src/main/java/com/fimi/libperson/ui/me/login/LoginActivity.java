package com.fimi.libperson.ui.me.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.host.ComonStaticURL;
import com.fimi.host.HostConstants;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.language.RegionManage;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.DensityUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.entity.ImageSource;
import com.fimi.libperson.ivew.IThirdLoginView;
import com.fimi.libperson.presenter.ThirdLoginPresenter;
import com.fimi.libperson.ui.me.register.RegisterActivity;
import com.fimi.libperson.ui.web.UserProtocolWebViewActivity;
import com.fimi.libperson.widget.BitmapLoadTaskInstance;
import com.fimi.libperson.widget.LargeView;
import com.fimi.widget.DialogManager;
import com.fimi.widget.NetworkLoadManage;

import router.Router;


public class LoginActivity extends BaseActivity implements IThirdLoginView, BitmapLoadTaskInstance.OnLoadListener {
    private static final String TAG = "LoginActivity";
    Button mBtnLogin;
    Button mBtnRegister;
    ImageView mIvFacebook;
    ImageView mIvLogo;
    ImageView mIvMi;
    ImageView mIvTwitter;
    RelativeLayout mRlLogin;
    TextView mTvNoLogin;
    TextView mTvRegion;
    RelativeLayout rlFimiLogin;
    RelativeLayout rlThirdLogin;
    private DialogManager mDialogManager;
    private LargeView mLargeView;
    private ThirdLoginPresenter mLoginPresenter;
    private RegionManage mRegionManage;

    @Override
    public void initData() {
        getWindow().setFlags(1024, 1024);
        this.mIvLogo = findViewById(R.id.iv_logo);
        this.mBtnLogin = findViewById(R.id.btn_login);
        this.mBtnRegister = findViewById(R.id.btn_register);
        this.mIvTwitter = findViewById(R.id.iv_twitter);
        this.mIvFacebook = findViewById(R.id.iv_facebook);
        this.mIvMi = findViewById(R.id.iv_mi);
        this.mRlLogin = findViewById(R.id.rl_login);
        this.mTvNoLogin = findViewById(R.id.tv_no_login);
        this.mTvRegion = findViewById(R.id.tv_region);
        this.mTvRegion.setText(getSpannableString());
        this.rlThirdLogin = findViewById(R.id.rl_third_login);
        this.rlFimiLogin = findViewById(R.id.rl_fimi_login);
        FontUtil.changeFontLanTing(getAssets(), this.mBtnLogin, this.mBtnRegister, this.mTvRegion, this.mTvNoLogin);
        if (Constants.productType == ProductEnum.X8S) {
            this.mTvNoLogin.setVisibility(View.VISIBLE);
        } else {
            Constants.isRefreshMainView = true;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.rlFimiLogin.getLayoutParams();
            layoutParams.bottomMargin = DensityUtil.dip2px(this, 150.0f);
            this.mTvNoLogin.setVisibility(View.GONE);
        }
        this.mLoginPresenter = new ThirdLoginPresenter(this);
        this.mLargeView = findViewById(R.id.large_view);
        this.mLargeView.setImage(BitmapLoadTaskInstance.getInstance().getBitmap());
        BitmapLoadTaskInstance.getInstance().setOnLoadListener(this);
        if (BitmapLoadTaskInstance.getInstance().getBitmap() == null) {
            BitmapLoadTaskInstance.getInstance().setImage(ImageSource.asset("login_bg.jpg"), this.mContext);
        }
        this.mDialogManager = new DialogManager(this.mContext, getString(R.string.register_select_service_title), getSpannableStringSecurity(), getString(R.string.ensure), getString(R.string.cancel));
        this.mDialogManager.setClickOutIsCancle(true);
        this.mDialogManager.setSpan(true);
    }

    @Override
    public void doTrans() {
        this.mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.mDialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
                        @Override
                        public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                            SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                            LoginActivity.this.readyGo(LoginMainActivity.class);
                        }

                        @Override
                        public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                        }
                    });
                    LoginActivity.this.mDialogManager.showDialog();
                    return;
                }
                LoginActivity.this.readyGo(LoginMainActivity.class);
            }
        });
        this.mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.mDialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
                        @Override
                        public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                            SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                            LoginActivity.this.readyGo(RegisterActivity.class);
                        }

                        @Override
                        public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                        }
                    });
                    LoginActivity.this.mDialogManager.showDialog();
                    return;
                }
                LoginActivity.this.readyGo(RegisterActivity.class);
            }
        });
        this.mIvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.mDialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
                        @Override
                        public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                            SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                            LoginActivity.this.mLoginPresenter.loginFacebook();
                        }

                        @Override
                        public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                        }
                    });
                    LoginActivity.this.mDialogManager.showDialog();
                    return;
                }
                LoginActivity.this.mLoginPresenter.loginFacebook();
            }
        });
        this.mIvTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.mDialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
                        @Override
                        public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                            SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                            LoginActivity.this.mLoginPresenter.loginTwitter();
                        }

                        @Override
                        public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                        }
                    });
                    LoginActivity.this.mDialogManager.showDialog();
                    return;
                }
                LoginActivity.this.mLoginPresenter.loginTwitter();
            }
        });
        this.mIvMi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
                    LoginActivity.this.mDialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
                        @Override
                        public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                            SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                            LoginActivity.this.mLoginPresenter.loginMi();
                        }

                        @Override
                        public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                        }
                    });
                    LoginActivity.this.mDialogManager.showDialog();
                    return;
                }
                LoginActivity.this.mLoginPresenter.loginMi();
            }
        });
        this.mTvNoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPStoreManager.getInstance().saveInt(Constants.SP_PERSON_USER_TYPE, Constants.UserType.Guest.ordinal());
                Intent it = Router.invoke(LoginActivity.this, "activity://app.main");
                LoginActivity.this.readyGoThenKillAllActivity(it);
            }
        });
        this.mTvRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.isShowUserProtocol = true;
                Intent intent = Router.invoke(LoginActivity.this.mContext, "activity://person.service");
                intent.putExtra("is_setting", false);
                LoginActivity.this.startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void setStatusBarColor() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isShowUserProtocol && !SPStoreManager.getInstance().getBoolean(HostConstants.USER_PROTOCOL, false)) {
            this.mDialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
                @Override
                public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                    SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, true);
                }

                @Override
                public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                }
            });
            this.mDialogManager.showDialog();
            Constants.isShowUserProtocol = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            this.mTvRegion.setText(getSpannableString());
        } else {
            this.mLoginPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void loginThirdListener(boolean isSuccess, String msg) {
        NetworkLoadManage.dismiss();
        if (!isSuccess && msg != null) {
            ToastUtil.showToast(this.mContext, msg, 1);
        }
    }

    @Override
    public void updateProgress(boolean isShow) {
        if (isShow) {
            NetworkLoadManage.show(this);
        } else {
            NetworkLoadManage.dismiss();
        }
    }

    @Override
    public void loginSuccess() {
        Constants.isRefreshMainView = true;
        Intent it = Router.invoke(this, "activity://app.main");
        readyGoThenKillAllActivity(it);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mLargeView != null) {
            this.mLargeView.setRecyle();
        }
        BitmapLoadTaskInstance.getInstance().setRecyle();
    }

    @Override
    public void onStop() {
        super.onStop();
        NetworkLoadManage.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        NetworkLoadManage.dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private SpannableString getSpannableStringSecurity() {
        String str1 = this.mContext.getString(R.string.register_select_service_login);
        String str2 = this.mContext.getString(R.string.register_select_service2_login);
        String str3 = this.mContext.getString(R.string.register_select_service3);
        String str4 = this.mContext.getString(R.string.register_select_service4_login);
        String str6 = this.mContext.getString(R.string.register_select_service6);
        SpannableString spannableString = new SpannableString(str1 + str2 + str3 + str4 + str6);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.libperson_ecurity_label)), 0, str1.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.libperson_ecurity_label)), str1.length() + str2.length(), str1.length() + str2.length() + str3.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.libperson_ecurity_label)), str1.length() + str2.length() + str3.length() + str4.length(), str1.length() + str2.length() + str3.length() + str4.length() + str6.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(LoginActivity.this.getResources().getColor(R.color.libperson_ecurity));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                LoginActivity.this.goWebActivity(ComonStaticURL.getPolicyUrl(), LoginActivity.this.getString(R.string.person_setting_user_agreement));
            }
        }, str1.length(), str1.length() + str2.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(LoginActivity.this.getResources().getColor(R.color.libperson_ecurity));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                LoginActivity.this.goWebActivity(ComonStaticURL.getPrivacyUrl(), LoginActivity.this.getString(R.string.person_setting_user_privacy));
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

    private SpannableString getSpannableString() {
        this.mRegionManage = new RegionManage();
        String string = getString(ServiceItem.getServicename()[this.mRegionManage.getCountryType()]);
        String str1 = this.mContext.getString(R.string.libperson_select);
        SpannableString spannableString = new SpannableString(str1 + string);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.libperson_region_label)), 0, str1.length(), 33);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(LoginActivity.this.mContext.getResources().getColor(R.color.libperson_region));
            }

            @Override
            public void onClick(View widget) {
            }
        }, str1.length(), str1.length() + string.length(), 33);
        return spannableString;
    }

    @Override
    public void onComplete() {
        if (this.mLargeView != null && !isFinishing() && BitmapLoadTaskInstance.getInstance().getBitmap() != null) {
            this.mLargeView.setImage(BitmapLoadTaskInstance.getInstance().getBitmap());
        }
    }
}

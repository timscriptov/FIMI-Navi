package com.fimi.libperson.ui.web;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.fimi.android.app.R;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.kernel.utils.AbViewUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.NetworkDialog;

/* loaded from: classes.dex */
public class UserProtocolWebViewActivity extends BasePersonActivity {
    private static final String TAG = "UserProtocolWebViewActi";
    NetworkDialog mNetworkDialog;
    private Button mBtnBack;
    private TitleView mTitleView;
    private WebView webView;

    @Override // com.fimi.libperson.BasePersonActivity, com.fimi.kernel.base.BaseActivity
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.activity_user_protocol_web_view;
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        initView();
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();
        params.setMargins(0, this.statusBarHeight + this.marginStatus, 0, 0);
        this.mTitleView.setLayoutParams(params);
    }

    private void initView() {
        final String url = getIntent().getStringExtra("web_url");
        String title = getIntent().getStringExtra("web_title");
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(title);
        this.mBtnBack = (Button) findViewById(R.id.btn_back);
        this.webView = (WebView) findViewById(R.id.web_view);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        if (AbViewUtil.getScreenHeight(this) >= 2000) {
            this.webView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
        }
        this.mNetworkDialog = new NetworkDialog(this.mContext, R.style.network_load_progress_dialog, true);
        this.webView.setWebViewClient(new WebViewClient() { // from class: com.fimi.libperson.ui.web.UserProtocolWebViewActivity.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url2) {
                view.loadUrl(url2);
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProtocolWebViewActivity.this);
                builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() { // from class: com.fimi.libperson.ui.web.UserProtocolWebViewActivity.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() { // from class: com.fimi.libperson.ui.web.UserProtocolWebViewActivity.1.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() { // from class: com.fimi.libperson.ui.web.UserProtocolWebViewActivity.2
            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView view, int newProgress) {
                if (!((UserProtocolWebViewActivity) UserProtocolWebViewActivity.this.mContext).isFinishing()) {
                    if (newProgress == 100) {
                        if (UserProtocolWebViewActivity.this.mNetworkDialog != null) {
                            try {
                                UserProtocolWebViewActivity.this.mNetworkDialog.dismiss();
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        return;
                    }
                    try {
                        if (!UserProtocolWebViewActivity.this.mNetworkDialog.isShowing()) {
                            UserProtocolWebViewActivity.this.mNetworkDialog.show();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        this.webView.postDelayed(new Runnable() { // from class: com.fimi.libperson.ui.web.UserProtocolWebViewActivity.3
            @Override // java.lang.Runnable
            public void run() {
                UserProtocolWebViewActivity.this.webView.loadUrl(url);
            }
        }, 500L);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            finish();
        }
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
        this.mTitleView.setIvLeftListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.web.UserProtocolWebViewActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (UserProtocolWebViewActivity.this.webView.canGoBack()) {
                    UserProtocolWebViewActivity.this.webView.goBack();
                } else {
                    UserProtocolWebViewActivity.this.finish();
                }
            }
        });
    }
}

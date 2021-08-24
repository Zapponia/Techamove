package com.techamove.view.WebView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.techamove.R;
import com.techamove.Utils.Constants;
import com.techamove.Utils.LoadingDialog;
import com.techamove.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {

    Context mContext;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.webView)
    WebView webView;
    String strIdentification = "", strSocialUrl = "";
    LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        mContext = WebViewActivity.this;
        strIdentification = getIntent().getStringExtra(Constants.IDENTIFICATION);
        loadingDialog = new LoadingDialog(mContext);
        showData();
    }

    private void showData() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new myWebClient());
        if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.VIEW_BUSINESS_CARD)) {
            imgDrawer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_white_left));
            strSocialUrl = getIntent().getStringExtra(Constants.SOCIAL_URL);
            txtTitle.setText(mContext.getResources().getString(R.string.lable_social));
            if (!strSocialUrl.contains("https://")) {

                webView.loadUrl("https://" + strSocialUrl);
            }

        } else {
            txtTitle.setText(mContext.getResources().getString(R.string.menu_privacypolicy));
            webView.loadUrl(Constants.Privacy_Url);
        }
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDrawer();
    }

    @OnClick(R.id.imgDrawer)
    public void onClickBack() {
        if (!TextUtils.isEmpty(strIdentification) && strIdentification.equals(Constants.VIEW_BUSINESS_CARD)) {
            onBackPressed();
        } else {
            openDrawer();
        }
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if (!((Activity) mContext).isFinishing()) {
                if (loadingDialog != null && !loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            loadingDialog.dismiss();
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            String message = "SSL Certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate authority is not trusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate Hostname mismatch.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }
            message += " Do you want to continue anyway?";

            builder.setTitle("SSL Certificate Error");
            builder.setMessage(message);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

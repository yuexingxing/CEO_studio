package com.tajiang.ceo.mess.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by work on 2016/7/12.
 */
public class OrderRewardActivity extends BaseActivity {

    @BindView(R.id.playProgressBar)
    ProgressBar playProgressBar;
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected void initTopBar() {
        setTitle("订单奖励");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_order_reward);
        initWebView();
    }

    private void initWebView() {

        String URL = "http://www.itajiang.com/orderReward/Order_Reward.html";

        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setJavaScriptEnabled(true);
        saveData(mWebSettings);
        newWin(mWebSettings);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(URL);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void newWin(WebSettings mWebSettings) {
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }
    private void saveData(WebSettings mWebSettings) {
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
    }
    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };


    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }
        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }
        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebView);
            resultMsg.sendToTarget();
            return true;
        }
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog dialog = new AlertDialog.Builder(OrderRewardActivity.this).setMessage(message).setPositiveButton("确定", null).create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                playProgressBar.setVisibility(View.INVISIBLE);
            } else {
                if (playProgressBar.getVisibility() == View.INVISIBLE) {
                    playProgressBar.setVisibility(View.VISIBLE);
                }
                playProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    };
}

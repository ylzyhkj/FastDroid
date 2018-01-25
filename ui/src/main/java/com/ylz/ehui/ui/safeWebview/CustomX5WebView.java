package com.ylz.ehui.ui.safeWebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Author: yms
 * Date: 2018/1/25
 * Description: 使用腾讯X5内核的WebView
 */
public class CustomX5WebView extends WebView implements CustomChromeClient.OnWebChromeListener, CustomChromeClient.OnReceivedTitleListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private OnReceiveErrorListener mOnReceiveErrorListener;

    @SuppressLint("SetJavaScriptEnabled")
    public CustomX5WebView(Context context) {
        this(context, null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public CustomX5WebView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public CustomX5WebView(Context context, AttributeSet attr, int type) {
        super(context, attr, type);
        initParams();
        initWebViewSettings();
        getView().setClickable(true);
    }

    private void initParams() {
        setWebViewClient(new WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
    }

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        // 使用DOM缓存机制
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mSwipeRefreshLayout == null) {
            return;
        }

        mSwipeRefreshLayout.setEnabled(getWebScrollY() == 0);
    }

    public void attachSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public void setOnReceiveErrorListener(OnReceiveErrorListener listener) {
        mOnReceiveErrorListener = listener;
    }

    public void reloadUrl() {
        reload();
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

    }

    @Override
    public void onReceivedTitle(String title) {

    }

    public interface OnReceiveErrorListener {
        void onReceiveError(int errorCode);
    }
}


package com.noxstudio.fruitcas.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.applinks.AppLinkData;
import com.noxstudio.fruitcas.android.util.Receiver;

public class WebViewAcivity extends AppCompatActivity {

    private WebView mWebView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);

        preferences = getSharedPreferences("WebView", Context.MODE_PRIVATE);
        editor = preferences.edit();

        mWebView = findViewById(R.id.webView);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new MyWebViewClient());

        String url = getIntent().getStringExtra("URL");

        AppLinkData.fetchDeferredAppLinkData(WebViewAcivity.this, new AppLinkData.CompletionHandler() {
            @Override
            public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                try {
                    String[] params = appLinkData.getTargetUri().toString().split("://");
                    if (params.length > 0) {
                        editor.putString("parameters", params[1].replaceAll("\\?", "&"));
                        editor.apply();
                        editor.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (!Receiver.referrer.isEmpty()) {
            editor.putString("referrer", Receiver.referrer.replaceAll("-", "&").replaceAll("%3D", "="));
            editor.apply();
            editor.commit();
        }
        if (!preferences.getString("referrer", "").isEmpty() && preferences.getString("referrer", "").contains("pid")) {
            // грузим с Receiver.referrer параметрами
            mWebView.loadUrl(url + "&" + preferences.getString("referrer", ""));
        } else {
            // грузим с Facebook deep link параметрами (пункт 6)
            mWebView.loadUrl(url + preferences.getString("parameters", "&source=organic&pid=1"));
        }


//        mWebView.loadUrl(url+preferences.getString("parameters", "&source=organic&pid=1"));
    }

    private class MyWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        // Для старых устройств
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else {
        super.onBackPressed();
        }
    }
}

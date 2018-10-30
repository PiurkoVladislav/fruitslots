package com.noxstudio.fruitcas.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.noxstudio.fruitcas.android.util.ParsingHelper;
import com.noxstudio.fruitcas.android.util.Results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WebViewAcivity extends AppCompatActivity {

    private WebView mWebView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ParsingHelper mHelper;
    private List<Results> mResults = new ArrayList<>();
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);

        preferences = getSharedPreferences("Main", Context.MODE_PRIVATE);
        editor = preferences.edit();
        mHelper = new ParsingHelper("http://23myappserver.bid/alternate2/index.php");


        mWebView = findViewById(R.id.webView);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new MyWebViewClient());

        String url = getIntent().getStringExtra("URL");

//        if (!Receiver.referrer.isEmpty()) {
//            editor.putString("referrer", Receiver.referrer.replaceAll("-", "&").replaceAll("%3D", "="));
//            editor.apply();
//            editor.commit();
//        }
//        if (!preferences.getString("referrer", "").isEmpty() && preferences.getString("referrer", "").contains("pid")) {
////             грузим с Receiver.referrer параметрами
//            mWebView.loadUrl(url + "&" + preferences.getString("referrer", ""));
//        } else {
////             грузим с Facebook deep link параметрами (пункт 6)
//            mWebView.loadUrl(url + preferences.getString("parameters", "&source=organic&pid=1"));
//        }


        mWebView.loadUrl(url+preferences.getString("parameters", "&source=organic&pid=1"));
        Log.i("WEB_VIEW",url+preferences.getString("parameters", "&source=organic&pid=1"));

    }


    @Override
    protected void onResume() {
        super.onResume();

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();

        mTimer.schedule(mMyTimerTask,120000,120000);
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



    public void logConversionEvent (int goal, int payout) {
        Bundle bundle = new Bundle();
        bundle.putInt("goal", goal);
        bundle.putInt("payout", payout);
        Main.getAppLogger().logEvent("conversion", bundle);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                mResults = mHelper.getMasResult(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(mResults!=null) {
                for (Results results : mResults) {
                    logConversionEvent(results.getGoal(), results.getPayout());
                    Log.i("RUN", String.valueOf(results.getGoal()) + ", " + String.valueOf(results.getPayout()));
                }
            }
            };
        }
}

package com.noxstudio.fruitcas.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.noxstudio.fruitcas.android.cons.Constants;
import com.noxstudio.fruitcas.android.util.ParsingHelper;
import com.noxstudio.fruitcas.android.util.User;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
import com.yandex.metrica.push.YandexMetricaPush;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;



public class Main extends AppCompatActivity {


    private TelephonyManager telManager;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;
    private int id;
    private String country;
    private int timeZone;
    private ParsingHelper mParsingHelper;
    private User mUser;
    private AsyncRequest mAsyncRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        telManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        getCountry();

        mySharedPreferences = getSharedPreferences("Main", Context.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
        mParsingHelper = new ParsingHelper("http://true.noxstudio.club/index.php");

        mAsyncRequest = new AsyncRequest();

        try {
            mUser = mAsyncRequest.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AppLinkData.fetchDeferredAppLinkData(Main.this, new AppLinkData.CompletionHandler() {
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

//preferences.getString("parameters", "&source=organic&pid=1")
//строку выше добавляем к ссылке в webView.loadUrl как в пункте 6
            }
        });

        if (mUser!=null){
            saveSettings(mUser.getId());
            Intent intent = new Intent(this,WebViewAcivity.class);
            intent.putExtra("URL",mUser.getResult()+mySharedPreferences.getString("parameters", "&source=organic&pid=1"));
            intent.putExtra("ID",mUser.getId());
            startActivity(intent);
            Log.i("Main", mUser.getResult());
            Log.i("Main", String.valueOf(timeZone));
            Log.i("Main", country);

        }else {
            Intent intent = new Intent(this,AndroidLauncher.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            YandexMetrica.resumeSession(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            YandexMetrica.pauseSession(this);
        }
    }

    private void saveSettings(int param){
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(Constants.APP_PREFERENCES_ID, param);
        editor.apply();
    }

    private boolean getSetting(){
        if(mySharedPreferences.contains(Constants.APP_PREFERENCES_ID)) {
            id = (mySharedPreferences.getInt(Constants.APP_PREFERENCES_ID, 0));
            return true;
        }else {
            return false;
        }
    }

    private void getCountry(){
        if(telManager.getSimCountryIso().toUpperCase()!=null) {
            country = telManager.getSimCountryIso().toUpperCase();
        }else if(telManager.getNetworkCountryIso().toUpperCase()!=null){
            country = telManager.getNetworkCountryIso().toUpperCase();
        }else if(getResources().getConfiguration().locale.getCountry().toUpperCase()!=null){
            country = getResources().getConfiguration().locale.getCountry().toUpperCase();
        }
        timeZone = Calendar.getInstance().getTimeZone().getRawOffset();

    }



    class AsyncRequest extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... voids) {
            User user = new User();
            if(getSetting()){
                try {
                    user = mParsingHelper.getResultWithoutId(country,timeZone,id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    user = mParsingHelper.getResult(country,timeZone);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return user;
        }
        @Override
        protected void onPostExecute(User user) {
            mUser = user;

        }
    }


}

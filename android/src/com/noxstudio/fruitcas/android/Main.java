package com.noxstudio.fruitcas.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.noxstudio.fruitcas.android.cons.Constants;
import com.noxstudio.fruitcas.android.util.ParsingHelper;
import com.noxstudio.fruitcas.android.util.User;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class Main extends AppCompatActivity {


    private TelephonyManager telManager;
    private SharedPreferences mySharedPreferences;
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

        // Создание расширенной конфигурации библиотеки.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(Constants.API_KEY).build();
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this.getApplication());

        telManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        getCountry();

        mySharedPreferences = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        mParsingHelper = new ParsingHelper("http://true.noxstudio.club/index.php");

        mAsyncRequest = new AsyncRequest();

        try {
            mUser = mAsyncRequest.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mUser!=null){
            saveSettings(mUser.getId());
            Intent intent = new Intent(this,WebViewAcivity.class);
            intent.putExtra("URL",mUser.getResult());
            startActivity(intent);
        }else {
            Intent intent = new Intent(this,AndroidLauncher.class);
            startActivity(intent);
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

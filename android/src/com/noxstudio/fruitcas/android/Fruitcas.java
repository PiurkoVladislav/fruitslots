package com.noxstudio.fruitcas.android;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.noxstudio.fruitcas.android.cons.Constants;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
import com.yandex.metrica.push.YandexMetricaPush;

import java.util.Map;

public class Fruitcas extends Application {

    private static AppEventsLogger logger;

    @Override
    public void onCreate() {
        super.onCreate();

        AppsFlyerConversionListener conversionDataListener =
                new AppsFlyerConversionListener() {

                    @Override
                    public void onInstallConversionDataLoaded(Map<String, String> map) {

                    }

                    @Override
                    public void onInstallConversionFailure(String s) {

                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> map) {

                    }

                    @Override
                    public void onAttributionFailure(String s) {

                    }
                };
        AppsFlyerLib.getInstance().init(Constants.AF_DEV_KEY, conversionDataListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);

        // Создание расширенной конфигурации библиотеки.
//        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(Constants.API_KEY).build();
//        // Инициализация AppMetrica SDK.
//        YandexMetrica.activate(getApplicationContext(), config);

        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder(Constants.API_KEY);
        YandexMetrica.activate(getApplicationContext(), configBuilder.build());

        YandexMetricaPush.init(getApplicationContext());

        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this);


        Log.i("Fruitcas", configBuilder.toString());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        logger = AppEventsLogger.newLogger(this);
    }

    public static AppEventsLogger getAppLogger() {
        return logger;
    }


}

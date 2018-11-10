package com.noxstudio.fruitcas.android;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerTrackingRequestListener;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.noxstudio.fruitcas.android.cons.Constants;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
import com.yandex.metrica.push.YandexMetricaPush;

import java.util.Map;

public class Fruitcas extends MultiDexApplication {

    private static AppEventsLogger logger;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        AppsFlyerConversionListener conversionDataListener =
                new AppsFlyerConversionListener() {

                    @Override
                    public void onInstallConversionDataLoaded(Map<String, String> map) {
                        for (String attrName : map.keySet()) {
                            Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + map.get(attrName));
                        }
                    }

                    @Override
                    public void onInstallConversionFailure(String s) {
                        Log.i(Fruitcas.class.getName(),"onInstallConversionFailure");
                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> map) {
                        Log.i(Fruitcas.class.getName(),"onAppOpenAttribution");
                    }

                    @Override
                    public void onAttributionFailure(String s) {
                        Log.i(Fruitcas.class.getName(),"onAttributionFailure");
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

        //Google Analytics
        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    public static AppEventsLogger getAppLogger() {
        return logger;
    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }


}

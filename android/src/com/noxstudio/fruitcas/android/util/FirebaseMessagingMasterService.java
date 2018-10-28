package com.noxstudio.fruitcas.android.util;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yandex.metrica.push.firebase.MetricaMessagingService;

public class FirebaseMessagingMasterService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message) {
        // AppMetrica автоматически распознает свои сообщения и обработает только их.
        new MetricaMessagingService().processPush(this, message);

        // Реализуйте логику отправки сообщений в другие SDK.
    }
}

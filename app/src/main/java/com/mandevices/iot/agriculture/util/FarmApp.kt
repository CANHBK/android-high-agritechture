package com.mandevices.iot.agriculture.util

import android.app.Activity
import android.app.Application
import com.mandevices.iot.agriculture.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import timber.log.Timber
import javax.inject.Inject


class FarmApp : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
//    var clientId = ""
//    lateinit var client: MqttAndroidClient
//    lateinit var mqttConnectOptions: MqttConnectOptions

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
        Timber.plant(Timber.DebugTree())
//        clientId = MqttClient.generateClientId()
//        client = MqttAndroidClient(applicationContext, MqttConfig.MQTT_SERVER_URI , clientId)
//        mqttConnectOptions = MqttConnectOptions()
//        mqttConnectOptions.apply {
//            this.userName = MqttConfig.MQTT_USER_NAME
//            this.password = MqttConfig.MQTT_PASSWORD.toCharArray()
//        }
    }

    override fun activityInjector() = dispatchingAndroidInjector
}

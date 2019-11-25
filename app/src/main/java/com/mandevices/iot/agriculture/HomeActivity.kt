package com.mandevices.iot.agriculture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mandevices.iot.agriculture.util.FarmApp
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.paperdb.Paper
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    @Inject
    lateinit var client: MqttAndroidClient

    @Inject
    lateinit var mqttConnectOptions: MqttConnectOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Paper.init(applicationContext);

        initConnect()

    }

    private fun initConnect() {
        Timber.d("init mqtt")

        try {
            val token = client.connect(mqttConnectOptions)
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT", "Success")
                    client.subscribe("AUTO_UPDATED_ENVIRONMENT_PARAMS", 2)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("MQTT", "Failed")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}

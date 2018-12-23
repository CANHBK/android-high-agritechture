package com.mandevices.iot.agriculture.di

import android.content.Context
import androidx.room.Room
import com.mandevices.iot.agriculture.db.*
import com.mandevices.iot.agriculture.util.MqttConfig
import com.mandevices.iot.agriculture.util.NetworkState
import dagger.Module
import dagger.Provides
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun db(context: Context): SmartFarmDB {
        return Room.databaseBuilder(context, SmartFarmDB::class.java, "smart-farm.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun gateDao(db: SmartFarmDB): GateDao {
        return db.gateDao()
    }

    @Singleton
    @Provides
    fun userDao(db: SmartFarmDB): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun monitorDao(db: SmartFarmDB): MonitorDao {
        return db.monitorDao()
    }

    @Singleton
    @Provides
    fun controlDao(db: SmartFarmDB): ControlDao {
        return db.controlDao()
    }

    @Singleton
    @Provides
    fun relayDao(db: SmartFarmDB): RelayDao {
        return db.relayDao()
    }
    @Singleton
    @Provides
    fun sensorDataDao(db: SmartFarmDB): SensorDataDao {
        return db.sensorDataDao()
    }

    @Singleton
    @Provides
    fun sensorDao(db: SmartFarmDB): SensorDao {
        return db.sensorDao()
    }

    @Singleton
    @Provides
    fun monitorWithSensorsDao(db: SmartFarmDB): MonitorWithSensorsDao {
        return db.monitorWithSensorDao()
    }

    @Singleton
    @Provides
    fun networkState(context: Context): NetworkState {
        return NetworkState(context)
    }

    @Singleton
    @Provides
    fun mqttClient(context: Context): MqttAndroidClient {
        val clientId = MqttClient.generateClientId()
        val client = MqttAndroidClient(context, MqttConfig.MQTT_SERVER_URI, clientId)
        return client
    }

    @Singleton
    @Provides
    fun mqttConnectionOptions(context: Context): MqttConnectOptions {
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.apply {
            this.userName = MqttConfig.MQTT_USER_NAME
            this.password = MqttConfig.MQTT_PASSWORD.toCharArray()
        }
        return mqttConnectOptions
    }
}
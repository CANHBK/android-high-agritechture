package com.mandevices.iot.agriculture.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mandevices.iot.agriculture.vo.*

@Database(
        entities = [Gate::class,User::class,Monitor::class,Control::class,Relay::class,SensorData::class,Sensor::class],
        version = 17,
        exportSchema = true
)
abstract class SmartFarmDB : RoomDatabase() {
    abstract fun gateDao(): GateDao
    abstract fun userDao(): UserDao
    abstract fun monitorDao():MonitorDao
    abstract fun controlDao():ControlDao
    abstract fun relayDao():RelayDao
    abstract fun sensorDataDao():SensorDataDao
}
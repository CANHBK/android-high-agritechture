package com.mandevices.iot.agriculture.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mandevices.iot.agriculture.vo.Gate
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.User

@Database(
        entities = [Gate::class,User::class,Monitor::class],
        version = 4,
        exportSchema = true
)
abstract class SmartFarmDB : RoomDatabase() {
    abstract fun gateDao(): GateDao
    abstract fun userDao(): UserDao
    abstract fun monitorDao():MonitorDao
}
package com.example.administrator.glasshouse.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Monitor
import com.example.administrator.glasshouse.vo.User

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
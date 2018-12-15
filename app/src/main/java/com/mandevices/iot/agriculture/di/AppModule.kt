package com.mandevices.iot.agriculture.di

import android.content.Context
import androidx.room.Room
import com.mandevices.iot.agriculture.db.*
import com.mandevices.iot.agriculture.util.NetworkState
import dagger.Module
import dagger.Provides
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
    fun networkState(context: Context): NetworkState {
        return NetworkState(context)
    }
}
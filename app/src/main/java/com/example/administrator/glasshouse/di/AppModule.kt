package com.example.administrator.glasshouse.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.administrator.glasshouse.db.GateDao
import com.example.administrator.glasshouse.db.SmartFarmDB
import com.example.administrator.glasshouse.db.UserDao
import com.example.administrator.glasshouse.util.NetworkState
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
    fun networkState(context: Context): NetworkState {
        return NetworkState(context)
    }
}
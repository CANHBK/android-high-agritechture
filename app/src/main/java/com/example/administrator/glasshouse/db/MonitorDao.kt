package com.example.administrator.glasshouse.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import com.example.administrator.glasshouse.vo.Monitor

@Dao
interface MonitorDao {
    fun allMonitors(serviceTag:String):LiveData<List<Monitor>>
}
package com.mandevices.iot.agriculture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mandevices.iot.agriculture.vo.MonitorWithSensors


@Dao
abstract class MonitorWithSensorsDao {

    @Query("SELECT * from monitor")
    abstract fun monitorWithSensors(): LiveData<List<MonitorWithSensors>>
}
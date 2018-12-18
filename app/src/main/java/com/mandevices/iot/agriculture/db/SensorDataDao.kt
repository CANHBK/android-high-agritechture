package com.mandevices.iot.agriculture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.SensorData


@Dao
abstract class SensorDataDao : BaseDao<SensorData> {


    @Query(
            """
        SELECT * FROM sensordata
        WHERE monitorTag = :tag and year=:year and month=:month and day = :day
"""
    )
    abstract fun loadSensorDataByDate(tag: String, year: Int, month: Int, day: Int): LiveData<SensorData>
}
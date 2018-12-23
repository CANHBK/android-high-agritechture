package com.mandevices.iot.agriculture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.Sensor
import com.mandevices.iot.agriculture.vo.SensorData


@Dao
abstract class SensorDao : BaseDao<Sensor> {

    @Query("DELETE FROM sensor" )
    abstract fun deleteAllRecord()

//    @Query("UPDATE sensor SET name = :name WHERE tag = :tag")
//    abstract fun updateNameByTag(name:String,tag: String)
//
//    @Query("UPDATE monitor SET sensors = :sensors WHERE tag = :tag")
//    abstract fun updateMonitorData(sensors:String,tag: String)

//    @Query("UPDATE monitor SET lastTemp = :temp,lastLight =: light, lastAirHumi=: WHERE tag = :tag")
//    abstract fun updateLastMonitorData(temp:Int,light:Int,tag: String)

    @Query(
            """
        SELECT * FROM sensor
        WHERE tag = :tag
        ORDER BY name ASC"""
    )
    abstract fun loadSensors(tag: String): LiveData<List<Sensor>>

//    @Query(
//            """
//        SELECT * FROM monitor
//        WHERE se = :tag
//        ORDER BY name ASC"""
//    )
//    abstract fun loadSensor(sensorId: String): LiveData<Monitor>

}
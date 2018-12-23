package com.mandevices.iot.agriculture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.SensorData


@Dao
abstract class MonitorDao : BaseDao<Monitor> {

    @Query("DELETE FROM monitor" )
    abstract fun deleteAllRecord()

    @Query("UPDATE monitor SET name = :name WHERE tag = :tag")
    abstract fun updateNameByTag(name:String,tag: String)




//    @Query("UPDATE monitor SET lastTemp = :temp,lastLight =: light, lastAirHumi=: WHERE tag = :tag")
//    abstract fun updateLastMonitorData(temp:Int,light:Int,tag: String)

    @Query(
            """
        SELECT * FROM monitor
        WHERE serviceTag = :serviceTag
        ORDER BY name ASC"""
    )
    abstract fun loadMonitors(serviceTag: String): LiveData<List<Monitor>>

    @Query(
            """
        SELECT * FROM monitor
        WHERE tag = :tag
        ORDER BY name ASC"""
    )
    abstract fun loadMonitor(tag: String): LiveData<Monitor>

}
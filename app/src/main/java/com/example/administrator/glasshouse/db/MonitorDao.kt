package com.example.administrator.glasshouse.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.administrator.glasshouse.vo.Monitor


@Dao
abstract class MonitorDao : BaseDao<Monitor> {

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
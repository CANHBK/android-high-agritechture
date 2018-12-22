package com.mandevices.iot.agriculture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mandevices.iot.agriculture.vo.Control
import com.mandevices.iot.agriculture.vo.Monitor


@Dao
abstract class ControlDao : BaseDao<Control> {

    @Query(
            """
        SELECT * FROM control
        WHERE serviceTag = :serviceTag
        ORDER BY name ASC"""
    )
    abstract fun loadControls(serviceTag: String): LiveData<List<Control>>

    @Query(
            """
        SELECT * FROM control
        WHERE tag = :tag
        ORDER BY name ASC"""
    )

    abstract fun loadControl(tag: String): LiveData<Control>
    @Query("DELETE FROM control" )
   abstract fun deleteAllRecord()
}
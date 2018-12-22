package com.mandevices.iot.agriculture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mandevices.iot.agriculture.vo.Control
import com.mandevices.iot.agriculture.vo.Monitor
import com.mandevices.iot.agriculture.vo.Relay


@Dao
abstract class RelayDao : BaseDao<Relay> {

    @Query("DELETE FROM relay" )
    abstract fun deleteAllRecord()
    @Query(
            """
        SELECT * FROM relay
        WHERE controlTag = :tag
        ORDER BY name ASC"""
    )
    abstract fun loadRelays(tag: String): List<Relay>

    @Query(
            """
        SELECT * FROM relay
        WHERE controlTag = :tag AND `index`=:index
        ORDER BY name ASC"""
    )
    abstract fun loadRelay(tag: String,index:Int): Relay
}
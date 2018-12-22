package com.mandevices.iot.agriculture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mandevices.iot.agriculture.vo.Gate

@Dao
interface   GateDao :BaseDao<Gate>{

    @Query(
            """
        SELECT * FROM gate
        WHERE owner = :userId"""
    )
    fun loadGates(userId: String): LiveData<List<Gate>>

    @Query(
            """
        SELECT * FROM gate
        WHERE id = :idGate
        ORDER BY name ASC"""
    )
    fun loadGate(idGate: String): LiveData<Gate>

    @Query("DELETE FROM gate" )
    fun deleteAllRecord()

}
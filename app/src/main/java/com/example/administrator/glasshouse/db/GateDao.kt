package com.example.administrator.glasshouse.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.administrator.glasshouse.vo.Gate

@Dao
interface   GateDao :BaseDao<Gate>{

    @Query(
            """
        SELECT * FROM gate
        WHERE owner = :userId
        ORDER BY name ASC"""
    )
    fun loadGates(userId: String): LiveData<List<Gate>>

    @Query(
            """
        SELECT * FROM gate
        WHERE id = :idGate
        ORDER BY name ASC"""
    )
    fun loadGate(idGate: String): LiveData<Gate>

}
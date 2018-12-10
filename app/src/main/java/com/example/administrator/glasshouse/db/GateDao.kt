package com.example.administrator.glasshouse.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.administrator.glasshouse.vo.Gate

@Dao
interface   GateDao :BaseDao<Gate>{
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertGates(gates: List<Gate>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertGate(gates: Gate)
//
//    @Query("DELETE FROM gate WHERE owner = :userId")
//    fun deleteByUserId(userId: String)
//
//    @Query("DELETE FROM gate WHERE serviceTag = :serviceTag")
//    fun deleteByServiceTag(serviceTag: String)
//
//    @Query("UPDATE gate SET name=:name WHERE serviceTag = :serviceTag")
//    fun updateGate(name: String,serviceTag: String)

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
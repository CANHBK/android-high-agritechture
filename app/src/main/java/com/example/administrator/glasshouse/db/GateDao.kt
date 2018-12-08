package com.example.administrator.glasshouse.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.administrator.glasshouse.vo.Gate

@Dao
interface GateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGates(gates: List<Gate>)

    @Query(
            """
        SELECT * FROM gate
        WHERE userId = :userId
        ORDER BY name DESC"""
    )
    fun loadGates(userId: String): LiveData<List<Gate>>

}
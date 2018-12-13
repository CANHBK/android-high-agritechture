package com.example.administrator.glasshouse.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.administrator.glasshouse.vo.User

@Dao
interface UserDao : BaseDao<User> {

    @Query("DELETE FROM user")
    fun resetTable()
    @Query(
            """
        SELECT * FROM user
        """
    )
    fun loadUser(): LiveData<User>


}
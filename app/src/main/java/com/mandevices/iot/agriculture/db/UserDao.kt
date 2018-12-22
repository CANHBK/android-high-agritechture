package com.mandevices.iot.agriculture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mandevices.iot.agriculture.vo.User

@Dao
interface UserDao : BaseDao<User> {
    @Query("DELETE FROM user" )
    abstract fun deleteAllRecord()

    @Query("DELETE FROM user")
    fun resetTable()
    @Query(
            """
        SELECT * FROM user
        """
    )
    fun loadUser(): LiveData<User>


}
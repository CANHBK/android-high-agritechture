package com.example.administrator.glasshouse.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.administrator.glasshouse.vo.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query(
            """
        SELECT * FROM user
        WHERE email = :email
        """
    )
    fun loadUser(email: String): LiveData<User>


}
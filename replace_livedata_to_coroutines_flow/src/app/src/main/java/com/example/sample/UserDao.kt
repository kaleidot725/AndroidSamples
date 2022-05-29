package com.example.sample

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user : User)

    @Insert
    suspend fun insertAll(users : List<User>)

    @Delete
    suspend fun delete(user : User)

    @Query("delete from users")
    suspend fun deleteAll()

    @Query("select * from users")
    fun getAll(): Flow<List<User>>
}
package jp.kaleidot725.sample.room.dao

import androidx.room.Dao
import androidx.room.Query
import jp.kaleidot725.sample.room.view.UserRepo

@Dao
interface UserRepoDao {
    @Query("select * from UserRepo")
    fun getAll(): List<UserRepo>

    @Query("select * from UserRepo where id = :id")
    fun getUserRepo(id: Int): UserRepo
}

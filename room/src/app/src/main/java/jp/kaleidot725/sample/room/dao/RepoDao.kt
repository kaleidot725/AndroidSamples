package jp.kaleidot725.sample.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jp.kaleidot725.sample.room.data.Repo
import jp.kaleidot725.sample.room.data.User

@Dao
interface RepoDao {
    @Insert
    fun insert(repo : Repo)

    @Update
    fun update(repo : Repo)

    @Delete
    fun delete(repo : Repo)

    @Query("delete from repos")
    fun deleteAll()

    @Query("select * from repos")
    fun getAll(): List<Repo>

    @Query("select * from repos where userId = :userId")
    fun getRepo(userId: Int): Repo
}

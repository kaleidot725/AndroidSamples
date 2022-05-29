package jp.kaleidot725.sample.room

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.kaleidot725.sample.room.dao.RepoDao
import jp.kaleidot725.sample.room.data.User
import jp.kaleidot725.sample.room.dao.UserDao
import jp.kaleidot725.sample.room.dao.UserRepoDao
import jp.kaleidot725.sample.room.data.Repo
import jp.kaleidot725.sample.room.view.UserRepo

@Database(entities = [User::class, Repo::class], views = [UserRepo::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao
    abstract fun userRepoDao(): UserRepoDao
}

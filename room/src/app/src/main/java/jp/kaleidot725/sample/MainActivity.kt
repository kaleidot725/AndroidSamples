package jp.kaleidot725.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import jp.kaleidot725.sample.room.AppDatabase
import jp.kaleidot725.sample.room.data.Repo
import jp.kaleidot725.sample.room.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {
    private val database : AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            testForUserDao()
            testForRepoDao()
            testForUserRepo()
        }
    }

    private fun testForUserDao() {
        val userDao = database.userDao()

        val newUser = User(0, Date().time.toString(), Date().time.toString())
        userDao.insert(newUser)
        Log.v("TAG", "after insert ${userDao.getAll()}")

        userDao.delete(newUser)
        Log.v("TAG", "after delete ${userDao.getAll()}")
    }

    private fun testForRepoDao() {
        val repoDao = database.repoDao()

        val newRepo = Repo(0, Date().time.toString())
        repoDao.insert(newRepo)
        Log.v("TAG", "after insert ${repoDao.getAll()}")

        repoDao.delete(newRepo)
        Log.v("TAG", "after delete ${repoDao.getAll()}")
    }

    private fun testForUserRepo() {
        val userDao = database.userDao()
        val repoDao = database.repoDao()
        val userRepoDao = database.userRepoDao()

        val newUser = User(0, Date().time.toString(), Date().time.toString())
        userDao.insert(newUser)
        Log.v("TAG", "after insert ${userDao.getAll()}")

        val newRepo = Repo(newUser.id, Date().time.toString())
        repoDao.insert(newRepo)
        Log.v("TAG", "after insert ${repoDao.getAll()}")

        Log.v("TAG", "get view ${userRepoDao.getAll()}")

        userDao.delete(newUser)
        Log.v("TAG", "after delete ${userDao.getAll()}")

        repoDao.delete(newRepo)
        Log.v("TAG", "after delete ${repoDao.getAll()}")

        Log.v("TAG", "get view ${userRepoDao.getAll()}")
    }
}

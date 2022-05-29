package com.example.sample

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDao) {
    suspend fun fetchData() {
        userDao.deleteAll()
        userDao.insertAll(SAMPLE_USERS)
    }

    val users: Flow<List<User>> = userDao.getAll()

    companion object {
        private val SAMPLE_USERS = listOf(
            User(1, "A", "G", 10),
            User(2, "B", "F", 8),
            User(3, "C", "E", 20),
            User(4, "D", "D", 25),
            User(5, "E", "C", 59),
            User(6, "F", "B", 9),
            User(7, "G", "A", 1)
        )
    }
}
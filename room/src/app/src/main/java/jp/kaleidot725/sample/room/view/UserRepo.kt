package jp.kaleidot725.sample.room.view

import androidx.room.Database
import androidx.room.DatabaseView
import androidx.room.PrimaryKey

@DatabaseView(
    viewName = "UserRepo",
    value = """
        SELECT users.id, users.lastName, users.firstName,
        repos.name AS repoName FROM users
        INNER JOIN repos ON users.id = repos.userId
    """
)
data class UserRepo(
    val id: Int,
    val lastName: String?,
    val firstName: String?,
    val repoName: String?
)

package jp.kaleidot725.sample.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey val userId: Int,
    val name: String?
)

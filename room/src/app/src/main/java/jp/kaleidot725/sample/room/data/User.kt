package jp.kaleidot725.sample.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val firstName: String?,
    val lastName: String?
)

package jp.kaleidot725.sample

import java.util.*

data class User(val firstName: String, val lastName: String, val age: Int) {
    val id = UUID.randomUUID().toString()
}
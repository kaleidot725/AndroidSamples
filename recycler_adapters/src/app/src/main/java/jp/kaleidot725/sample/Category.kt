package jp.kaleidot725.sample

import java.util.*

data class Category(val title: String) {
    val id = UUID.randomUUID().toString()
}
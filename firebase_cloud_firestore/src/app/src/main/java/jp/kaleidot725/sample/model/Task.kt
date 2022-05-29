package jp.kaleidot725.sample.model

import java.util.*

data class Task(val id: String, val time: Long, val name: String) {
    companion object {
        fun create(name: String): Task = Task(UUID.randomUUID().toString(), Date().time, name)
    }
}

fun Task.toMap(): Map<String, *> {
    return hashMapOf(
        "id" to this.id,
        "time" to this.time,
        "name" to this.name
    )
}

fun Map<String, Any>.toTask(): Task {
    val id = this["id"] as String
    val time = this["time"] as Long
    val name = this["name"] as String
    return Task(id, time, name)
}
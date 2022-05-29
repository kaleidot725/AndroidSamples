package jp.kaleidot725.sample.data.entity

data class Tag(
    val name: String,
    val versions: List<String>
)

val nullTag = Tag("", listOf())
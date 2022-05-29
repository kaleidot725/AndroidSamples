package jp.kaleidot725.sample.data.entity

data class Group(
    val created_at: String, val id: Int, val name: String,
    val `private`: Boolean, val updated_at: String, val url_name: String
)

val nullGroup = Group("", 0, "", false, "", "")
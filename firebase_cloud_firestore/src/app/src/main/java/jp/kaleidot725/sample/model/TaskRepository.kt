package jp.kaleidot725.sample.model

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TaskRepository {
    private val database : FirebaseFirestore get() = FirebaseFirestore.getInstance()

    suspend fun add(task: Task): Boolean {
        try {
            val collection = database.collection(COLLECTION_PATH)
            val document = collection.document(task.id)
            val data = task.toMap()
            document.set(data).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun delete(task: Task): Boolean {
        try {
            val collection = database.collection(COLLECTION_PATH)
            val document = collection.document(task.id)
            document.delete().await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun fetchTask(limit: Long): List<Task> {
        try {
            val collection = database.collection(COLLECTION_PATH)
            val query = collection.limit(limit)
            val documents = query.get().await().documents
            val dataList = documents.map { it.data }
            return dataList.mapNotNull { it?.toTask() }
        } catch (e: Exception) {
            return listOf()
        }
    }

    companion object {
        private const val COLLECTION_PATH = "tasks"
    }
}
package jp.kaleidot725.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import jp.kaleidot725.sample.model.Task
import jp.kaleidot725.sample.model.TaskRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val repository: TaskRepository = TaskRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            // ①現在のタスク一覧を取得する
            println("①現在のタスク一覧を取得する ▶ " + repository.fetchTask(100))

            // ②新しいタスクを追加する
            val newTask = Task.create("New Task")
            repository.add(newTask)
            println("②新しいタスクを追加する ▶ " + repository.fetchTask(100))

            // ③新しく追加したタスクを削除する
            repository.delete(newTask)
            println("③新しく追加したタスクを削除する ▶ " + repository.fetchTask(100))
        }
    }
}

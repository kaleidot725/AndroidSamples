package jp.kaleidot725.koinnavigationcompose.ui.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class MainViewModel : ViewModel() {
    val title = "HOME"
    val createdAt = Date().time

    private val _count: MutableStateFlow<Int> = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun increment() {
        _count.value = _count.value + 1
    }

    fun decrement() {
        _count.value = _count.value - 1
    }
}

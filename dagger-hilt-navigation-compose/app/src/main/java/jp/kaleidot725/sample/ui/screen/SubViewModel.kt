package jp.kaleidot725.sample.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SubViewModel @Inject constructor() : ViewModel() {
    val title = "SUB"
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

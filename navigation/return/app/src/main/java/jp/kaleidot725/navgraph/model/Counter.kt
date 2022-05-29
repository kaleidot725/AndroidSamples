package jp.kaleidot725.navgraph.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Counter {
    private val _count :MutableStateFlow<Int> = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun increment() {
        _count.value = _count.value + 1
    }

    fun decrement() {
        _count.value = _count.value - 1
    }
}
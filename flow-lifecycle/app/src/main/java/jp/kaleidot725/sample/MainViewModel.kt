package jp.kaleidot725.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class MainViewModel: ViewModel() {
    val count: Flow<Int> = flow {
        var count = 0
        while (true) {
            delay(2000)
            count += 1
            emit(count)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )
}
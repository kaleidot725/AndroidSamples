package jp.kaleidot725.sample.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.kaleidot725.sample.model.CounterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoubleCountScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CounterRepository
): ViewModel() {
    private val _count: MutableStateFlow<Int> = MutableStateFlow(repository.count)
    val count: StateFlow<Int> = _count

    fun plus() {
        viewModelScope.launch {
            repository.plus()
            repository.plus()
            _count.emit(repository.count)
        }
    }

    fun minus() {
        viewModelScope.launch {
            repository.minus()
            repository.minus()
            _count.emit(repository.count)
        }
    }
}

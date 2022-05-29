package jp.kaleidot725.navgraph.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.kaleidot725.navgraph.model.Counter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val counter: Counter
): ViewModel() {
    private val _countLiveData: MutableLiveData<Int> = MutableLiveData()
    val countLiveData: LiveData<Int> = _countLiveData

    init {
        counter.count.onEach { count ->
            _countLiveData.value = count
        }.launchIn(viewModelScope)
    }

    fun increment() {
        counter.increment()
    }

    fun decrement() {
        counter.decrement()
    }
}
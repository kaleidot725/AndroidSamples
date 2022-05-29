package jp.kaleidot725.sample.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.kaleidot725.sample.model.FooUsecase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val usecase: FooUsecase
): ViewModel() {
    fun print() {
        usecase.print("MainViewModel", "Event")
    }
}

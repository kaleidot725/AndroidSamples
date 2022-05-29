package jp.kaleidot725.sample.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.util.*
import javax.inject.Inject

data class MainState(
    val title: String = "HOME",
    val createAt: Long = Date().time,
    val count: Long = 0
)

sealed class MainSideEffect {
    object Navigate : MainSideEffect()
}

@HiltViewModel
class MainViewModel @Inject constructor() : ContainerHost<MainState, MainSideEffect>, ViewModel() {
    override val container = container<MainState, MainSideEffect>(MainState())

    fun increment() = intent {
        reduce {
            state.copy(count = state.count + 1)
        }
    }

    fun decrement() = intent {
        reduce {
            state.copy(count = state.count - 1)
        }
    }

    fun navigate() = intent {
        postSideEffect(MainSideEffect.Navigate)
    }
}

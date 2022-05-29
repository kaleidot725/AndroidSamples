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

data class SubState(
    val title: String = "SUB",
    val createAt: Long = Date().time,
    val count: Long = 0
)

sealed class SubSideEffect {
    object PopBack : SubSideEffect()
}

@HiltViewModel
class SubViewModel @Inject constructor() : ContainerHost<SubState, SubSideEffect>, ViewModel() {
    override val container = container<SubState, SubSideEffect>(SubState())

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

    fun popBack() = intent {
        postSideEffect(SubSideEffect.PopBack)
    }
}

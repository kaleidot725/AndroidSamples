package jp.kaleidot725.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilitySample() {
    var editable by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "AnimatedVisibility",
            style = MaterialTheme.typography.h6
        )

        AnimatedVisibility(visible = editable) {
            Surface(
                color = Color.Yellow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            ) {}
        }

        Button(
            onClick = { editable = !editable },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Toggle")
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityStateSample() {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "AnimatedVisibilityState",
            style = MaterialTheme.typography.h6
        )

        AnimatedVisibility(visibleState = state) {
            Surface(
                color = Color.Yellow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            ) {
                Text(
                    text = state.getAnimationState().toString()
                )
            }
        }

        Button(
            onClick = { state.targetState = !state.currentState },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Toggle")
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityEnterExitSample() {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "AnimatedVisibilityState",
            style = MaterialTheme.typography.h6
        )

        AnimatedVisibility(
            visibleState = state,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            Surface(
                color = Color.Yellow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            ) {
                Text(text = state.getAnimationState().toString())
            }
        }

        Button(
            onClick = { state.targetState = !state.currentState },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Toggle")
        }
    }
}

enum class AnimState {
    VISIBLE,
    INVISIBLE,
    APPEARING,
    DISAPPEARING
}

fun MutableTransitionState<Boolean>.getAnimationState(): AnimState {
    return when {
        this.isIdle && this.currentState -> AnimState.VISIBLE
        !this.isIdle && this.currentState -> AnimState.DISAPPEARING
        this.isIdle && !this.currentState -> AnimState.INVISIBLE
        else -> AnimState.APPEARING
    }
}
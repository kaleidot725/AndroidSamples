package jp.kaleidot725.sample.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun SubScreen(viewModel: SubViewModel, onBack: () -> Unit) {
    val state by viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.popBack()
        Log.v("TEST", "Execute PopBack")

        launch {
            viewModel.container.sideEffectFlow.collect {
                when (it) {
                    SubSideEffect.PopBack -> {
                        onBack()
                        Log.v("TEST", "ReceiveA PopBack")
                    }
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.wrapContentSize(align = Alignment.Center)) {
            Text("TITLE: ${state.title}")
            Text("CREATED AT: ${state.createAt}")
            Text("COUNT: ${state.count}")
            Button(onClick = { viewModel.increment() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "INCREMENT")
            }
            Button(onClick = { viewModel.decrement() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "DECREMENT")
            }
            Button(onClick = { viewModel.popBack() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "Back Screen")
            }
        }
    }
}
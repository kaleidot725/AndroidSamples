package jp.kaleidot725.sample.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubScreen(viewModel: SubViewModel, onBack: () -> Unit) {
    val count by viewModel.count.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.wrapContentSize(align = Alignment.Center)) {
            Text("TITLE: ${viewModel.title}")
            Text("CREATED AT: ${viewModel.createdAt}")
            Text("COUNT: $count")
            Button(onClick = { viewModel.increment() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "INCREMENT")
            }
            Button(onClick = { viewModel.decrement() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "DECREMENT")
            }
            Button(onClick = { onBack() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "Back Screen")
            }
        }
    }
}
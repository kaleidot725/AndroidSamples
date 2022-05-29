package jp.kaleidot725.sample.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TwoScreen(viewModel: DoubleCountScreenViewModel) {
    val count by viewModel.count.collectAsState()

    Column {
        Text(text = "COUNT $count")
        Button(onClick = { viewModel.plus() }) {
            Text(text = "PLUS2")
        }
        Button(onClick = { viewModel.minus() }) {
            Text(text = "MINUS2")
        }
    }
}
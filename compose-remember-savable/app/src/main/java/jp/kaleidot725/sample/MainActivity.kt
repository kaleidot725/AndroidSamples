package jp.kaleidot725.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import jp.kaleidot725.sample.ui.theme.SampleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val mainState = MainState()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val count by mainState.count.collectAsState()
            SampleTheme {
                if (10 < count) {
                    CounterOver10(count, { mainState.plus() }, { mainState.minus() })
                } else {
                    CounterLessThan10(count, { mainState.plus() }, { mainState.minus() })
                }
            }
        }
    }
}

class MainState {
    private val _count: MutableStateFlow<Int> = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun plus() {
        _count.value = _count.value + 1
    }

    fun minus() {
        _count.value = _count.value - 1
    }
}

@Composable
private fun CounterOver10(count: Int, onPlus: () -> Unit, onMinus: () -> Unit) {
    var plusCount by rememberSaveable { mutableStateOf(0) }
    var minusCount by rememberSaveable { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Text(text = "CounterOver10", fontSize = 20.sp)
            Text(text = "Count $count PlusCount $plusCount MinusCount $minusCount")
            Button(
                onClick = {
                    plusCount++
                    onPlus.invoke()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Plus")
            }
            Button(
                onClick = {
                    minusCount--
                    onMinus.invoke()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Minus")
            }
        }
    }
}

@Composable
private fun CounterLessThan10(count: Int, onPlus: () -> Unit, onMinus: () -> Unit) {
    var plusCount by rememberSaveable { mutableStateOf(0) }
    var minusCount by rememberSaveable { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Text(text = "CounterLessThan10", fontSize = 20.sp)
            Text(text = "Count $count PlusCount $plusCount MinusCount $minusCount")
            Button(
                onClick = {
                    plusCount++
                    onPlus.invoke()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Plus")
            }
            Button(
                onClick = {
                    minusCount--
                    onMinus.invoke()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Minus")
            }
        }
    }
}
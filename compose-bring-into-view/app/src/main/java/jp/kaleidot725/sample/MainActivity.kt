package jp.kaleidot725.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kaleidot725.sample.ui.theme.SampleTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Yellow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(modifier = Modifier.wrapContentSize().padding(8.dp)) {
                        BringIntoViewDemo(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BringIntoViewDemo(modifier: Modifier) {
    val blueRequester = remember { BringIntoViewRequester() }
    val blackRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(
            Modifier
                .height(200.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Blue)
                    .bringIntoViewRequester(blueRequester)
            )
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Green)
            )
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Yellow)
            )
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Magenta)
            )
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Gray)
            )
            Box(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Black)
                    .bringIntoViewRequester(blackRequester)
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { coroutineScope.launch { blueRequester.bringIntoView() } }
        ) {
            Text("Bring Blue box into view")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { coroutineScope.launch { blackRequester.bringIntoView() } }
        ) {
            Text("Bring Black box into view")
        }
    }
}

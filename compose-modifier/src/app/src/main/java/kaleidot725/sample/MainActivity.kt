package kaleidot725.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kaleidot725.sample.ui.theme.SampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
//                    ClipText("ClipText")
//                    NoneClipText("NoneClipText")
//                    ClickableTextAfterPadding("after padding")
//                    ClickableTextBeforePadding("before padding")
                }
            }
        }
    }
}

@Composable
fun ClipText(name: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Hello $name!",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .clip(shape = RoundedCornerShape(32.dp))
                .background(Color.Yellow)
                .align(Alignment.Center),
        )
    }
}

@Composable
fun NoneClipText(name: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Hello $name!",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .background(Color.Yellow)
                .clip(shape = RoundedCornerShape(32.dp))
                .align(Alignment.Center),
        )
    }
}

@Composable
fun ClickableTextAfterPadding(name: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Hello $name!",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .background(Color.Yellow)
                .clickable { Log.v("TEST", "CLICK") }
                .padding(8.dp)
                .align(Alignment.Center),
        )
    }
}

@Composable
fun ClickableTextBeforePadding(name: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Hello $name!",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .background(Color.Yellow)
                .padding(8.dp)
                .clickable { Log.v("TEST", "CLICK") }
                .align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SampleTheme {
        ClipText("Android")
    }
}
package jp.kaleidot725.counter.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import jp.kaleidot725.counter.shared.MyCounter
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val counter = MyCounter(min = 0, max = 100)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var count by remember { mutableStateOf(counter.value) }

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.Center)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = count.toString(),
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            counter.plus()
                            count = counter.value
                        }
                    ) {
                        Text(text = "PLUS")
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            counter.minus()
                            count = counter.value
                        }
                    ) {
                        Text(text = "MINUS")
                    }
                }
            }
        }
    }
}

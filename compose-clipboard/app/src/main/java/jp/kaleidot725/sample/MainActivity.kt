package jp.kaleidot725.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val clipboardManager: ClipboardManager = LocalClipboardManager.current
            var editText: String by remember { mutableStateOf("") }

            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                TextField(
                    value = editText,
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    onValueChange = { editText = it }
                )

                Button(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    onClick = { clipboardManager.setText(AnnotatedString(editText)) }
                ) {
                    Text(text = "Copy Text")
                }

                Button(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    onClick = { editText = clipboardManager.getText()?.text ?: "" }
                ) {
                    Text(text = "Paste Text")
                }
            }
        }
    }
}

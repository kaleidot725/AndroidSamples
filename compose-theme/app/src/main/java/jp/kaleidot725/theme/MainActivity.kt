package jp.kaleidot725.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jp.kaleidot725.theme.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Screen()
        }
    }
}

@Composable
fun Screen() {
    AppTheme {
        Column {
            TopAppBar(
                title = { Text("Jetpack Compose Theme") },
                backgroundColor = MaterialTheme.colors.primarySurface
            )

            Surface(
                color = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    // CompositionLocalProvider を経由すると予め定義しておいた値を取得できる
                    // https://bit.ly/2UBXhTF
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = "H1 TEXT",
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h1
                        )
                    }

                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                        Text(
                            text = "H2 TEXT",
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h2
                        )
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    Screen()
}
package jp.kaleidot725.animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.kaleidot725.animation.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LazyColumn {
                        item {
                            AnimatedVisibilitySample()
                        }

                        item {
                            AnimatedVisibilityStateSample()
                        }

                        item {
                            AnimatedVisibilityEnterExitSample()
                        }

                        item {
                            AnimatedContentCounterDefault()
                        }

                        item {
                            AnimatedContentCounterCustom()
                        }

                        item {
                            AnimatedContentExpandableTextSample()
                        }

                        item {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            LazyColumn {
                item {
                    AnimatedVisibilitySample()
                }

                item {
                    AnimatedVisibilityStateSample()
                }

                item {
                    AnimatedVisibilityEnterExitSample()
                }
            }
        }
    }
}
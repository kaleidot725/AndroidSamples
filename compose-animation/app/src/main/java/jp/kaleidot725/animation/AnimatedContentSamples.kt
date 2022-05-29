package jp.kaleidot725.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun AnimatedContentCounterDefault() {
    Column {
        // targetState に渡した値が変更され、更新する必要があれば、アニメーション表示し更新する
        // (taregetState に渡した値をキーとして識別するような仕組みになっている)
        var count: Int by remember { mutableStateOf(0) }

        // デフォルトのアニメーションで現在の値がフェードアウトして、次の値がフェードインして表示されるようになっている
        AnimatedContent(targetState = count) { targetCount ->
            Text(
                text = "$targetCount",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { count++ }) {
                Text("PLUS")
            }

            Button(onClick = { count-- }) {
                Text("MINUS")
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedContentCounterCustom() {
    Column {
        var count: Int by remember { mutableStateOf(0) }

        AnimatedContent(
            targetState = count,
            transitionSpec = {
                // <EnterTransition> with <ExitTransition> という形式でアニメーションを定義する
                // 数がプラスされたか、マイナスされたかで移動する方向を変えたいので if で分岐している
                val isPlus = targetState > initialState
                if (isPlus) {
                    slideInHorizontally({ width -> width }) + fadeIn() with slideOutHorizontally({ width -> -width }) + fadeOut()
                } else {
                    slideInHorizontally({ width -> -width }) + fadeIn() with slideOutHorizontally({ width -> width }) + fadeOut()
                }
            }
        ) { targetCount ->
            Text(
                text = "$targetCount",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { count++ }) {
                Text("PLUS")
            }

            Button(onClick = { count-- }) {
                Text("MINUS")
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun AnimatedContentExpandableTextSample() {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colors.primary,
        onClick = { expanded = !expanded },
        modifier = Modifier.padding(8.dp)
    ) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn() with fadeOut() using SizeTransform { initialSize, targetSize ->
                    keyframes {
                        IntSize(initialSize.width, initialSize.height) at 250
                        durationMillis = 500
                    }
                }
            }
        ) { targetExpanded ->
            if (targetExpanded) {
                Text(
                    text = "Expanded",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            } else {
                Text(
                    text = "Not Expanded",
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }
}

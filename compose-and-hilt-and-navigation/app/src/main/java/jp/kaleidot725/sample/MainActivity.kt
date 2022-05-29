package jp.kaleidot725.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.kaleidot725.sample.ui.screen.CountScreenViewModel
import jp.kaleidot725.sample.ui.screen.DoubleCountScreenViewModel
import jp.kaleidot725.sample.ui.screen.OneScreen
import jp.kaleidot725.sample.ui.screen.TwoScreen
import jp.kaleidot725.sample.ui.theme.SampleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController, startDestination = "count") {
                        composable("count") {
                            val viewModel: CountScreenViewModel by viewModels()
                            OneScreen(viewModel)
                        }
                        composable("doubleCount") {
                            val viewModel: DoubleCountScreenViewModel by viewModels()
                            TwoScreen(viewModel)
                        }
                    }

                    Box {
                        Button(modifier = Modifier.align(Alignment.BottomCenter), onClick = {
                            val currentRoute = navController.currentDestination?.route
                            val nextRoute = if (currentRoute == "count") "doubleCount" else "count"
                            navController.navigate(nextRoute)
                        }) {
                            Text("Change mode")
                        }
                    }
                }
            }
        }
    }
}

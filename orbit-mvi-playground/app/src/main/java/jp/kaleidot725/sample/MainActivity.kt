package jp.kaleidot725.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.kaleidot725.sample.ui.screen.MainScreen
import jp.kaleidot725.sample.ui.screen.SubScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "main") {
                composable("main") {
                    MainScreen(
                        viewModel = hiltViewModel(),
                        onNext = {
                            navController.navigate("sub")
                        }
                    )
                }
                composable("sub") {
                    SubScreen(
                        viewModel = hiltViewModel(),
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

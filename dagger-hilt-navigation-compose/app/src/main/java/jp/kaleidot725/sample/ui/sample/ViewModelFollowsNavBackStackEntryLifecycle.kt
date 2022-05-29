package jp.kaleidot725.sample.ui.sample

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.kaleidot725.sample.ui.screen.MainScreen
import jp.kaleidot725.sample.ui.screen.SubScreen

@Composable
fun ViewModelFollowsNavBackStackEntryLifecycle() {
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
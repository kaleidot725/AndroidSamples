package jp.kaleidot725.koinnavigationcompose.ui.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.kaleidot725.koinnavigationcompose.ui.screen.MainScreen
import jp.kaleidot725.koinnavigationcompose.ui.screen.SubScreen
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

@Composable
fun getComposeViewModelOwner(): ViewModelOwner {
    return ViewModelOwner.from(
        LocalViewModelStoreOwner.current!!,
        LocalSavedStateRegistryOwner.current
    )
}

@Composable
inline fun <reified T : ViewModel> getComposeViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val viewModelOwner = getComposeViewModelOwner()
    return KoinJavaComponent.getKoin().getViewModel(qualifier, { viewModelOwner }, parameters)
}

@Composable
fun ViewModelFollowsNavBackStackEntryLifecycle() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                viewModel = getComposeViewModel(),
                onNext = {
                    navController.navigate("sub")
                }
            )
        }
        composable("sub") {
            SubScreen(
                viewModel = getComposeViewModel(),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
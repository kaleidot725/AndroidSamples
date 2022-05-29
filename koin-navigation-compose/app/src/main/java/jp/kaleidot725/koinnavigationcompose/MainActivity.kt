package jp.kaleidot725.koinnavigationcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import jp.kaleidot725.koinnavigationcompose.ui.sample.ViewModelFollowsNavBackStackEntryLifecycle
import jp.kaleidot725.koinnavigationcompose.ui.theme.KoinNavigationComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinNavigationComposeTheme {
                // ViewModelDoesntFollowLifecycle()
                // ViewModelFollowsActivityLifecycle(activity = this)
                ViewModelFollowsNavBackStackEntryLifecycle()
            }
        }
    }
}


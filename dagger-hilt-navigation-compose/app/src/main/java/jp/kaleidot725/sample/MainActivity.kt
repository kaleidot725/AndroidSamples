package jp.kaleidot725.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import jp.kaleidot725.sample.ui.sample.ViewModelFollowsNavBackStackEntryLifecycle
import jp.kaleidot725.sample.ui.theme.DaggerHiltNavigationComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DaggerHiltNavigationComposeTheme {
                // ViewModelDoesntFollowLifecycle()
                ViewModelFollowsNavBackStackEntryLifecycle()
            }
        }
    }
}

package jp.kaleidot725.sample.ui

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.kaleidot725.sample.R
import jp.kaleidot725.sample.model.FooUsecase
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @Inject lateinit var usecase: FooUsecase

    override fun onResume() {
        super.onResume()
        usecase.print("MainActivity", "Event")
    }
}

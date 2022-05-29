package kaleidot725.sample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kaleidot725.sample.R
import kaleidot725.sample.appModule
import kaleidot725.sample.databinding.ActivityMainBinding
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }

        val binding : ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        viewModel.users.observe(this, Observer {
            binding.mainText.text = it.toString()
        })

        viewModel.usersSortedByFirstName.observe(this, Observer {
            binding.sortFirstNameText.text = it.toString()
        })

        viewModel.usersSortedByLastName.observe(this, Observer {
            binding.sortLastNameText.text = it.toString()
        })

        viewModel.usersSortedByAge.observe(this, Observer {
            binding.sortAgeText.text = it.toString()
        })
    }
}

package com.example.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var binding: ActivityMainBinding? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.users.observe(this) { binding?.oneText?.text =  it.toString() }
        viewModel.usersSortedByFirstName.observe(this) { binding?.twoText?.text = it.toString() }
        viewModel.usersSortedByLastName.observe(this) { binding?.threeText?.text = it.toString() }
        viewModel.usersSortedByAge.observe(this) { binding?.fourText?.text = it.toString() }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    /**
     * LiveDataとStateFlowの違い by Kenji Abe より引用
     * https://star-zero.medium.com/livedata%E3%81%A8stateflow%E3%81%AE%E9%81%95%E3%81%84-5c141c6eb0f9
     */
    inline fun <T> StateFlow<T>.observe(owner: LifecycleOwner, crossinline onChanged: (T) -> Unit) {
        owner.lifecycleScope.launchWhenStarted {
            this@observe.collect { onChanged(it) }
        }
    }

}
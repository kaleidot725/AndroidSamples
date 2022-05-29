package jp.kaleidot725.sample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.kaleidot725.sample.R
import jp.kaleidot725.sample.databinding.ActivityMainBinding
import jp.kaleidot725.sample.di.ViewModelFactory
import jp.kaleidot725.sample.epoxy.ItemPagedListController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).also { binding ->
            val controller = ItemPagedListController()
            binding.epoxyRecyclerView.adapter = controller.adapter

            val layoutManager = LinearLayoutManager(applicationContext).apply { orientation = RecyclerView.VERTICAL }
            binding.epoxyRecyclerView.layoutManager = layoutManager

            val viewModel = ViewModelFactory.mainViewModel
            binding.viewModel = viewModel
            viewModel.entities.observe(this, Observer { controller.submitList(it) })
        }
    }
}

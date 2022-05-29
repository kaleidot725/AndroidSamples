package jp.kaleidot725.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.kaleidot725.sample.R
import jp.kaleidot725.sample.appModule
import jp.kaleidot725.sample.data.entity.Item
import jp.kaleidot725.sample.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val itemRecyclerAdapter = ItemRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(applicationContext).apply {
            orientation = RecyclerView.VERTICAL
        }
        binding.itemRecyclerView.adapter = itemRecyclerAdapter
        binding.itemRecyclerView.setHasFixedSize(true)
        viewModel.items.observe(this, androidx.lifecycle.Observer {
            itemRecyclerAdapter.submitList(it)
        })
    }
}

class ItemRecyclerAdapter() : PagedListAdapter<Item, ItemHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false) as View
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.title.text = getItem(position)?.title
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) =
                oldItem.id == newItem.id // check uniqueness

            override fun areContentsTheSame(oldItem: Item, newItem: Item) =
                oldItem == newItem // check contents
        }
    }
}

class ItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val title = view.title
}
package jp.kaleidot725.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.kaleidot725.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupConcatAdapter()
    }

    private fun setupListAdapter() {
        val userListAdapter = UserListAdapter()
        binding.recyclerView.adapter = userListAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        userListAdapter.submitList(
            listOf(
                User("あいざわ", "かずき", 29),
                User("ふじくら", "まさひろ", 52),
                User("よしずみ", "ひろゆき", 54),
                User("ほりのうち", "しんいち", 40),
                User("はすぬま", "よしろう", 37),
                User("はなわ", "のぶお", 38),
                User("おじま", "おじま", 31),
                User("しんざき", "くにひと", 35)
            )
        )
    }

    private fun setupConcatAdapter() {
        val categoryAdapter = CategoryAdapter()
        val userListAdapter = UserListAdapter()
        val concatAdapter = ConcatAdapter().apply {
            addAdapter(categoryAdapter)
            addAdapter(userListAdapter)
        }
        binding.recyclerView.adapter = concatAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        categoryAdapter.submitList(
            listOf(
                Category("人名図鑑")
            )
        )
        userListAdapter.submitList(
            listOf(
                User("あいざわ", "かずき", 29),
                User("ふじくら", "まさひろ", 52),
                User("よしずみ", "ひろゆき", 54),
                User("ほりのうち", "しんいち", 40),
                User("はすぬま", "よしろう", 37),
                User("はなわ", "のぶお", 38),
                User("おじま", "おじま", 31),
                User("しんざき", "くにひと", 35)
            )
        )
    }
}

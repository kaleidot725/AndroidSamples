package jp.kaleidot725.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.kaleidot725.sample.databinding.LayoutCategoryItemBinding

class CategoryItemViewHolder(
    private val binding: LayoutCategoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category) {
        binding.categoryTitleTextView.text = category.title
    }
}

val DIFF_UTIL_CATEGORY_ITEM_CALLBACK = object : DiffUtil.ItemCallback<Category>() {
    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }
}

class CategoryAdapter : ListAdapter<Category, CategoryItemViewHolder>(DIFF_UTIL_CATEGORY_ITEM_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val view = LayoutCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
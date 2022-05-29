package jp.kaleidot725.sample

import androidx.recyclerview.selection.ItemKeyProvider

class NumberKeyProvider(
    private val adapter: NumberAdapter
) : ItemKeyProvider<Number>(ItemKeyProvider.SCOPE_CACHED) {
    override fun getKey(position: Int): Number = adapter.getItem(position)
    override fun getPosition(key: Number): Int = adapter.getPosition(key)
}

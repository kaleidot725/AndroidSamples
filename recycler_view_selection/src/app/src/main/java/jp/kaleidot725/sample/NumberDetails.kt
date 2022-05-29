package jp.kaleidot725.sample

import androidx.recyclerview.selection.ItemDetailsLookup

data class NumberDetails(
    private val number: Number
) : ItemDetailsLookup.ItemDetails<Number>() {
    override fun getPosition(): Int = number.position
    override fun getSelectionKey(): Number? = number
}

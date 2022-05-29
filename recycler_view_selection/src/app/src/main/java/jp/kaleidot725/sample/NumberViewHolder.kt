package jp.kaleidot725.sample

import android.graphics.Color
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NumberViewHolder(private val root: FrameLayout) : RecyclerView.ViewHolder(root) {
    // View
    private val textView = root.findViewById<TextView>(R.id.simple_text_view)

    // Data
    private lateinit var number: Number

    fun bind(number: Number, isSelected: Boolean) {
        this.number = number
        this.textView.text = number.value
        this.root.setBackgroundColor(if (isSelected) Color.RED else Color.WHITE)
    }

    fun getItemDetails(): NumberDetails = NumberDetails(number)
}

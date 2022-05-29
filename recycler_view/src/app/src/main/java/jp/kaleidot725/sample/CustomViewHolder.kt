package jp.kaleidot725.sample

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {
    val textView : TextView = view.findViewById(R.id.text_view)

    fun bind(item: String) {
        textView.text = item
    }
}

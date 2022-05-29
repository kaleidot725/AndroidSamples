package jp.kaleidot725.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView

class NumberAdapter : RecyclerView.Adapter<NumberViewHolder>() {
    var data: List<Number> = listOf()
    var tracker: SelectionTracker<Number>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.simple_item, parent, false) as FrameLayout
        return NumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        val number = data[position]
        val isSelected = tracker?.isSelected(number) ?: false
        holder.bind(number, isSelected)
    }

    override fun getItemCount() = data.size

    fun getItem(position: Int) = data[position]

    fun getPosition(number: Number) = data.indexOf(number)
}

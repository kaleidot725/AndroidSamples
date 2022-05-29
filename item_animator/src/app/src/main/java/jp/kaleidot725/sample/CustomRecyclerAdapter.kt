package jp.kaleidot725.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.simple_item.view.*

class CustomViewHolder(val rootView: FrameLayout) : RecyclerView.ViewHolder(rootView) {
    val simpleTextView = rootView.findViewById<TextView>(R.id.simple_text_view)
}

class CustomRecyclerAdapter() : RecyclerView.Adapter<CustomViewHolder>() {
    var data : List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.simple_item, parent, false) as FrameLayout
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.rootView.simple_text_view.text = data[position]
    }

    override fun getItemCount() = data.size
}

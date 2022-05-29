package jp.kaleidot725.sample

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class NumberDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Number>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Number>? {
        return recyclerView.findChildViewUnder(e.x, e.y)?.let {
            (recyclerView.getChildViewHolder(it) as? NumberViewHolder)?.getItemDetails()
        }
    }
}

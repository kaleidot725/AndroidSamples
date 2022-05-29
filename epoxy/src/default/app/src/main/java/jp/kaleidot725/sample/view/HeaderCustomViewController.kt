package jp.kaleidot725.sample.view

import android.view.View
import com.airbnb.epoxy.Typed2EpoxyController

class HeaderCustomViewController(
    private val selectListener: SelectListener
) : Typed2EpoxyController<List<String>, Boolean>() {

    override fun buildModels(names: List<String>, loadingMore: Boolean) {
        names.forEach { item ->
            headerCustomView{
                id("Content")
                title(item)
                onClickListener(View.OnClickListener { selectListener.onSelected(item) })
            }
        }
    }

    interface SelectListener {
        fun onSelected(item: String)
    }
}
package jp.kaleidot725.sample.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import jp.kaleidot725.sample.LayoutEpoxyItemBindingModel_
import jp.kaleidot725.sample.data.entity.Item

class ItemPagedListController : PagedListEpoxyController<Item>() {
    override fun buildItemModel(currentPosition: Int, item: Item?): EpoxyModel<*> {
        requireNotNull(item)
        return LayoutEpoxyItemBindingModel_().apply {
            id(item.id)
            title(item.title)
        }
    }
}

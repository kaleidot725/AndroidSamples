package jp.kaleidot725.sample.controller

import com.airbnb.epoxy.Typed2EpoxyController
import jp.kaleidot725.sample.HeaderLayoutBindingModel_
import jp.kaleidot725.sample.contentLayout
import jp.kaleidot725.sample.headerLayout

data class Content(val uuid: String, val value: String)
data class Header(val uuid: String,val value: String)

class StickyHeaderController : Typed2EpoxyController<List<Header>, List<Content>>() {
    override fun buildModels(headers: List<Header>, contents: List<Content>) {
        headers.forEach { header ->
            headerLayout {
                id(header.uuid)
                title(header.value)
            }

            contents.forEach { content ->
                contentLayout {
                    id(content.uuid)
                    title(content.value)
                }
            }
        }
    }

    override fun isStickyHeader(position: Int): Boolean {
        return adapter.getModelAtPosition(position)::class == HeaderLayoutBindingModel_::class
    }
}

package jp.kaleidot725.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import jp.kaleidot725.sample.data.entity.Item
import jp.kaleidot725.sample.data.repository.ItemDataSourceFactory

class MainViewModel(factory: ItemDataSourceFactory) : ViewModel() {
    private val config = PagedList.Config.Builder().setInitialLoadSizeHint(10).setPageSize(10).build()
    val entities: LiveData<PagedList<Item>> = LivePagedListBuilder(factory, config).build()
}

package jp.kaleidot725.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import jp.kaleidot725.sample.data.entity.Item
import jp.kaleidot725.sample.data.entity.NetworkState
import jp.kaleidot725.sample.data.repository.ItemDataSourceFactory

class MainViewModel(private val itemDataSourcefactory: ItemDataSourceFactory): ViewModel() {
    private val config = PagedList.Config.Builder().setInitialLoadSizeHint(10).setPageSize(10).build()
    val items: LiveData<PagedList<Item>> = LivePagedListBuilder(itemDataSourcefactory, config).build()
}
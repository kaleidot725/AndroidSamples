package jp.kaleidot725.sample.data.repository

import androidx.paging.DataSource
import jp.kaleidot725.sample.data.entity.Item
import jp.kaleidot725.sample.data.service.QiitaService

class ItemDataSourceFactory(service: QiitaService) : DataSource.Factory<Int, Item>() {
    val source = ItemDataSource(service)

    override fun create(): DataSource<Int, Item> {
        return source
    }
}
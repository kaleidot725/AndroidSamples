package jp.kaleidot725.sample.di

import jp.kaleidot725.sample.data.repository.ItemDataSourceFactory
import jp.kaleidot725.sample.data.service.QiitaService
import jp.kaleidot725.sample.ui.MainViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ViewModelFactory {
    private val retrofit get() = Retrofit.Builder()
            .baseUrl("https://qiita.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val service = retrofit.create(QiitaService::class.java)

    val mainViewModel: MainViewModel get() = MainViewModel(ItemDataSourceFactory(service))
}

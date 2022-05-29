package jp.kaleidot725.navgraph.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import jp.kaleidot725.navgraph.model.Counter
import jp.kaleidot725.navgraph.viewmodel.CounterViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SigletonProvidesModule {

    @Provides
    @Singleton
    fun provideCounter(): Counter {
        return Counter()
    }
}
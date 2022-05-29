package jp.kaleidot725.sample

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jp.kaleidot725.sample.model.CounterRepository

@Module
@InstallIn(ViewModelComponent::class)
object ApplicationProvidesModule {
    @Provides
    @ViewModelScoped
    fun provideCounterRepository() : CounterRepository {
        return CounterRepository()
    }
}
package kaleidot725.sample

import androidx.room.Room
import kaleidot725.sample.data.Database
import kaleidot725.sample.data.UserRepository
import kaleidot725.sample.ui.MainViewModel
import org.koin.dsl.module
import java.lang.reflect.Array.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), Database::class.java, "users").build()
    }

    single {
        get<Database>().userDao()
    }

    single {
        UserRepository(get())
    }

    viewModel {
        MainViewModel(get())
    }
}

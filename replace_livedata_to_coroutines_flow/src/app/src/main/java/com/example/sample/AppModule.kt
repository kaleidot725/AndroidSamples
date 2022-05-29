package com.example.sample

import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), UserDatabase::class.java, "users").build()
    }

    factory {
        get<UserDatabase>().userDao()
    }

    factory {
        UserRepository(get())
    }

    viewModel {
        MainViewModel(get())
    }
}
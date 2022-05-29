package jp.kaleidot725.koinnavigationcompose

import jp.kaleidot725.koinnavigationcompose.ui.screen.MainViewModel
import jp.kaleidot725.koinnavigationcompose.ui.screen.SubViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainViewModel()
    }

    viewModel {
        SubViewModel()
    }
}
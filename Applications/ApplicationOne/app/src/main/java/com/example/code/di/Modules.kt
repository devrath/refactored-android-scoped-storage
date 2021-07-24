package com.example.code.di

import com.example.code.vm.SharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SharedViewModel(get()) }
}
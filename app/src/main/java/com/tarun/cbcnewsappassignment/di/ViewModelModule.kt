package com.tarun.cbcnewsappassignment.di

import com.tarun.cbcnewsappassignment.viewmodel.SharedNewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SharedNewsViewModel() }
}
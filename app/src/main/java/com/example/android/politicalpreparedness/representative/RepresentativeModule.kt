package com.example.android.politicalpreparedness.representative

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val representative = module {
    viewModel {
        RepresentativeViewModel(get())
    }
}

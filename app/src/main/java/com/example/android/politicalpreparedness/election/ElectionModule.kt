package com.example.android.politicalpreparedness.election

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val election = module {
    factory {
        provideElectionRepository(get(), get())
    }
    viewModel {
        ElectionsViewModel(get())
    }
}

private fun provideElectionRepository(
    dao: ElectionDao,
    rest: CivicsApiService
): ElectionRepository = ElectionRepositoryImpl(dao, rest)
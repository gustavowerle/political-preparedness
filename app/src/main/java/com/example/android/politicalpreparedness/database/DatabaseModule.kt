package com.example.android.politicalpreparedness.database

import androidx.room.Room
import org.koin.dsl.module

val database = module {
    single {
        Room.databaseBuilder(get(), ElectionDatabase::class.java, "elections").build()
    }
    single {
        get<ElectionDatabase>().electionDao
    }
}
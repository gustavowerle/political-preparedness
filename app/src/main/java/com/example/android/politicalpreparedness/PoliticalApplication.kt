package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.database
import com.example.android.politicalpreparedness.election.election
import com.example.android.politicalpreparedness.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PoliticalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PoliticalApplication)
            modules(
                database,
                networkModule,
                election
            )
        }
    }
}

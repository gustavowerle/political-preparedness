package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative

interface ElectionRepository {

    suspend fun getElections(): List<Election>

    suspend fun getSavedElections(): LiveData<List<Election>>

    suspend fun getRepresentatives(): List<Representative>

}

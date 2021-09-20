package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.representative.model.Representative

interface ElectionRepository {

    suspend fun getElections(): List<Election>

    suspend fun getVoterInfo(id: Int, address: String): VoterInfoResponse

    fun getSavedElectionsLiveData(): LiveData<List<Election>>

    suspend fun saveElection(election: Election)

    suspend fun removeSavedElection(election: Election)

    suspend fun isSavedElection(id: Int): Boolean

    suspend fun getRepresentatives(): List<Representative>

}

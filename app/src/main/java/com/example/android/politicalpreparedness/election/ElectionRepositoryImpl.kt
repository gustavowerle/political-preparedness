package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import retrofit2.HttpException

class ElectionRepositoryImpl(
    private val dao: ElectionDao,
    private val rest: CivicsApiService
) : ElectionRepository {

    override suspend fun getElections(): List<Election> {
        val result: List<Election> = try {
            rest.getElections().elections
        } catch (e: HttpException) {
            emptyList()
        }
        return result
    }

    override suspend fun getVoterInfo(id: Int, address: String): VoterInfoResponse {
        return rest.getVoterInfo(id, address)
    }

    override fun getSavedElectionsLiveData(): LiveData<List<Election>> {
        return dao.getAllElectionsLiveData()
    }

    override suspend fun saveElection(election: Election) {
        dao.insertElection(election)
    }

    override suspend fun removeSavedElection(election: Election) {
        dao.deleteElection(election)
    }

    override suspend fun isSavedElection(id: Int): Boolean {
        val result = dao.checkWhetherElectionIsSaved(id)
        return result > 0
    }

    override suspend fun getRepresentatives(address: Address): RepresentativeResponse {
        return rest.getRepresentativesByAddress(address.toFormattedString())
    }

}

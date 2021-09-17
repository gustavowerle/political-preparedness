package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative
import retrofit2.HttpException

class ElectionRepositoryImpl(
    private val dao: ElectionDao,
    private val rest: CivicsApiService
) : ElectionRepository {

    override suspend fun getElections(): List<Election> {
        val result: List<Election> = try {
            val response = rest.getElections().await()
            response.elections
        } catch (e: HttpException) {
            emptyList()
        }
        return result
    }

    override suspend fun getSavedElections(): LiveData<List<Election>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRepresentatives(): List<Representative> {
        TODO("Not yet implemented")
    }

}

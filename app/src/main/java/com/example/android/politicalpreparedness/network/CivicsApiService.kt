package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    @GET("elections")
    suspend fun getElections(): Deferred<ElectionResponse>

    //TODO: Add voterinfo API Call


    @GET("representatives")
    suspend fun getRepresentativesByAddress(
    ): Deferred<RepresentativeResponse>
}

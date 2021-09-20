package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */
interface CivicsApiService {

    @GET("elections")
    suspend fun getElections(): ElectionResponse

    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("electionId") electionId: Int,
        @Query("address") address: String,
        @Query("returnAllAvailableData") returnAllAvailableData: Boolean = true
    ): VoterInfoResponse

    @GET("representatives")
    suspend fun getRepresentativesByAddress(
    ): RepresentativeResponse

}

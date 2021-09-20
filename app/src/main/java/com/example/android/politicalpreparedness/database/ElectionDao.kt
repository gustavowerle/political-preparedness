package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.OnConflictStrategy
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    @Query("SELECT count(*) FROM election_table WHERE id = :id")
    suspend fun checkWhetherElectionIsSaved(id: Int): Int

    @Query("SELECT * FROM election_table")
    fun getAllElectionsLiveData(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElections(id: Int): Election

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM election_table")
    suspend fun clearElections()

}

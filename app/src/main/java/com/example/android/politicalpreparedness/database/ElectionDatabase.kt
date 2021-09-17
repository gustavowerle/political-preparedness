package com.example.android.politicalpreparedness.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.network.models.Election

@Database(entities = [Election::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ElectionDatabase : RoomDatabase() {

    abstract val electionDao: ElectionDao

}

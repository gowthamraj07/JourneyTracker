package com.gowthamraj07.journeytracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.gowthamraj07.journeytracker.data.db.entities.TripEntity
import com.gowthamraj07.journeytracker.data.db.entities.TripsWithPlaces
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Transaction
    @Query("SELECT * FROM trips")
    fun getTripsWithPlaces(): Flow<List<TripsWithPlaces>>

    @Insert
    fun insert(tripEntity: TripEntity): Long
}
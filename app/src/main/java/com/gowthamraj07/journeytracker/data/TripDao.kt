package com.gowthamraj07.journeytracker.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips")
    fun getTrips(): Flow<List<TripEntity>>

    @Transaction
    @Query("SELECT * FROM trips")
    fun getTripsWithPlaces(): Flow<List<TripsWithPlaces>>
}
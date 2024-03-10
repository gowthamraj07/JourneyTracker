package com.gowthamraj07.journeytracker.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places WHERE tripId = :tripId")
    fun getPlacesByTrip(tripId: Long): Flow<List<PlaceEntity>>
}
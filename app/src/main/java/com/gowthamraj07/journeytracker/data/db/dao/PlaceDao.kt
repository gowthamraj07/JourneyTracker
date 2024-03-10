package com.gowthamraj07.journeytracker.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.gowthamraj07.journeytracker.data.db.entities.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places WHERE tripId = :tripId")
    fun getPlacesByTrip(tripId: Int): Flow<List<PlaceEntity>>
}
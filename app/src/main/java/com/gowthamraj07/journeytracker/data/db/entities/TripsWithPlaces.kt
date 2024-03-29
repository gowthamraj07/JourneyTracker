package com.gowthamraj07.journeytracker.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TripsWithPlaces(
    @Embedded val trip: TripEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tripId"
    )
    val places: List<PlaceEntity>
)
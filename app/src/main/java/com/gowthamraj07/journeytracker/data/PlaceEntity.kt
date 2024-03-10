package com.gowthamraj07.journeytracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "places", foreignKeys = [
    ForeignKey(
        entity = TripEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tripId"),
        onDelete = ForeignKey.CASCADE
    )
])
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tripId") val tripId: Int,
    @ColumnInfo(name = "latitude" ) val latitude: Double,
    @ColumnInfo(name = "longitude" ) val longitude: Double,
)

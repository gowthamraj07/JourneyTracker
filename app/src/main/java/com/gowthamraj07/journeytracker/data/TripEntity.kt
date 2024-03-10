package com.gowthamraj07.journeytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val name: String
)
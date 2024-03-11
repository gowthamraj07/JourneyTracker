package com.gowthamraj07.journeytracker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    @ColumnInfo(name = "name" ) val name: String
)
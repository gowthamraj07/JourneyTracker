package com.gowthamraj07.journeytracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TripEntity::class], version = 1)
abstract class TripDatabase: RoomDatabase() {
    abstract fun tripDao(): TripDao
}
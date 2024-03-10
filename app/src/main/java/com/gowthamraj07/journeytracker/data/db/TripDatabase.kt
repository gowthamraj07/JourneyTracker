package com.gowthamraj07.journeytracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gowthamraj07.journeytracker.data.db.dao.TripDao
import com.gowthamraj07.journeytracker.data.db.entities.PlaceEntity
import com.gowthamraj07.journeytracker.data.db.entities.TripEntity

@Database(entities = [TripEntity::class, PlaceEntity::class], version = 1)
abstract class TripDatabase: RoomDatabase() {
    abstract fun tripDao(): TripDao
}
package com.gowthamraj07.journeytracker.domain.repository

import com.gowthamraj07.journeytracker.domain.Trip
import kotlinx.coroutines.flow.Flow

interface TripsRepository {
    fun getTrips(): Flow<List<Trip>>
}

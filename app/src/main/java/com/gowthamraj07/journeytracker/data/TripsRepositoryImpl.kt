package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import kotlinx.coroutines.flow.Flow

class TripsRepositoryImpl: TripsRepository {
    override fun getTrips(): Flow<List<Trip>> {
        TODO("Not yet implemented")
    }
}

package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TripsRepositoryImpl(private val tripDao: TripDao) : TripsRepository {
    override fun getTrips(): Flow<List<Trip>> {
        tripDao.getTrips()

        return flowOf(emptyList())
    }
}

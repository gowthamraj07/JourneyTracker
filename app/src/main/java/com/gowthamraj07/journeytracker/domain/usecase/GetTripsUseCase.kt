package com.gowthamraj07.journeytracker.domain.usecase

import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import kotlinx.coroutines.flow.Flow

class GetTripsUseCase(private val tripsRepository: TripsRepository) {
    suspend fun execute() : Flow<List<Trip>> {
        return tripsRepository.getTrips()
    }
}

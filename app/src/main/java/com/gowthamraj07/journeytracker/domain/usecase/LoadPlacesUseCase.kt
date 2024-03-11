package com.gowthamraj07.journeytracker.domain.usecase

import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow

class LoadPlacesUseCase(private val placesRepository: PlacesRepository) {
    suspend fun execute(tripId: Long): Flow<List<Place>> {
        return placesRepository.loadPlacesFor(tripId)
    }
}

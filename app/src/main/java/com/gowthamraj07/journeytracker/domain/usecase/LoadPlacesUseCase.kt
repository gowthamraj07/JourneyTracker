package com.gowthamraj07.journeytracker.domain.usecase

import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class LoadPlacesUseCase(private val placesRepository: PlacesRepository) {
    suspend fun execute(): Flow<List<Place>> {
        placesRepository.loadPlaces()
        return flowOf(emptyList())
    }

}

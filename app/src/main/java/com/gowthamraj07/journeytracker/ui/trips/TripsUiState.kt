package com.gowthamraj07.journeytracker.ui.trips

import com.gowthamraj07.journeytracker.domain.Trip
import kotlinx.coroutines.flow.Flow

sealed interface TripsUiState {
    data class Data(val trips: Flow<List<Trip>>) : TripsUiState

    data object Empty : TripsUiState
}

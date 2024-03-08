package com.gowthamraj07.journeytracker.ui.trips

sealed interface TripsUiState {
    data object Empty : TripsUiState
}

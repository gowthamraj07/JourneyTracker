package com.gowthamraj07.journeytracker.ui.trips

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TripsViewModel : ViewModel() {
    fun loadTrips() {
        TODO("Not yet implemented")
    }

    private val _uiState: StateFlow<TripsUiState> = MutableStateFlow(TripsUiState.Empty)
    val state: StateFlow<TripsUiState> = _uiState
}
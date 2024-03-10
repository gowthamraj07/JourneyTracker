package com.gowthamraj07.journeytracker.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gowthamraj07.journeytracker.domain.usecase.GetTripsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TripsViewModel(private val getTripsUseCase: GetTripsUseCase) : ViewModel() {
    fun loadTrips() {
        viewModelScope.launch {
            getTripsUseCase.execute().map {
                if (it.isEmpty()) {
                    TripsUiState.Empty
                } else {
                    TripsUiState.Data(getTripsUseCase.execute())
                }
            }.flowOn(Dispatchers.IO).collect {
                _uiState.value = it
            }
        }
    }

    private val _uiState: MutableStateFlow<TripsUiState> = MutableStateFlow(TripsUiState.Empty)
    val state: StateFlow<TripsUiState> = _uiState
}
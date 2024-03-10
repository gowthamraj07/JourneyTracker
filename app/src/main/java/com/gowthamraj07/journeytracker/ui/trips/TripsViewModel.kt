package com.gowthamraj07.journeytracker.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gowthamraj07.journeytracker.domain.usecase.GetTripsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TripsViewModel(private val getTripsUseCase: GetTripsUseCase,  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {
    fun loadTrips() {
        viewModelScope.launch {
            val tripsFlow = getTripsUseCase.execute()
            tripsFlow.flowOn(ioDispatcher).map {
                if (it.isEmpty()) {
                    TripsUiState.Empty
                } else {
                    TripsUiState.Data(tripsFlow)
                }
            }.collect { newState ->
                _uiState.update {
                    newState
                }
            }
        }
    }

    private val _uiState: MutableStateFlow<TripsUiState> = MutableStateFlow(TripsUiState.Empty)
    val state: StateFlow<TripsUiState> = _uiState
}
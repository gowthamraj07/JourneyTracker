package com.gowthamraj07.journeytracker.ui.ongoing.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.usecase.LoadPlacesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OngoingJourneyViewModel(private val loadPlacesUseCase: LoadPlacesUseCase, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {
    fun loadPlacesFor(tripId: Int) {
        viewModelScope.launch {
            val placesFlow = loadPlacesUseCase.execute(tripId)
            placesFlow.flowOn(ioDispatcher).map {
                OngoingJourneyUIState.Data(placesFlow)
            }.collect { newState ->
                _places.value = newState
            }
        }
    }

    private val _places: MutableStateFlow<OngoingJourneyUIState> =
        MutableStateFlow(OngoingJourneyUIState.Loading)
    val places: StateFlow<OngoingJourneyUIState> = _places
}

sealed interface OngoingJourneyUIState {
    data object Loading : OngoingJourneyUIState
    data class Data(val places: Flow<List<Place>>) : OngoingJourneyUIState
}


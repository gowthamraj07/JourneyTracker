package com.gowthamraj07.journeytracker.ui.ongoing.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.usecase.LoadPlacesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class OngoingJourneyViewModel(private val loadPlacesUseCase: LoadPlacesUseCase) : ViewModel() {
    fun loadPlaces() {
        viewModelScope.launch {
            loadPlacesUseCase.execute()
        }
    }

    val places: Flow<List<Place>> = flowOf(emptyList())
}

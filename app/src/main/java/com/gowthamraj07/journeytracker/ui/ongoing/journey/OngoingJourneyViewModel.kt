package com.gowthamraj07.journeytracker.ui.ongoing.journey

import androidx.lifecycle.ViewModel
import com.gowthamraj07.journeytracker.domain.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class OngoingJourneyViewModel : ViewModel() {
    fun loadPlaces() {
        TODO("Not yet implemented")
    }

    val places: Flow<List<Place>> = flowOf(emptyList())
}

package com.gowthamraj07.journeytracker.ui.start.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gowthamraj07.journeytracker.services.TripsServiceConnection
import com.gowthamraj07.journeytracker.ui.destinations.OngoingJourneyScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class StartJourneyViewModel(
    private val tripsServiceConnection: TripsServiceConnection
): ViewModel() {
    fun onScreenLoaded(navigator: DestinationsNavigator) {
        viewModelScope.launch {
            tripsServiceConnection.service?.tripId?.flowOn(Dispatchers.Default)?.collectLatest {
                if(it != 0L) {
                    navigator.navigate(OngoingJourneyScreenDestination(tripId = it))
                }
            }
        }
    }
}

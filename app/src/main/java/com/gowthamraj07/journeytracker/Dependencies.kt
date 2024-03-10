package com.gowthamraj07.journeytracker

import com.gowthamraj07.journeytracker.data.TripsRepositoryImpl
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import com.gowthamraj07.journeytracker.domain.usecase.GetTripsUseCase
import com.gowthamraj07.journeytracker.ui.ongoing.journey.OngoingJourneyViewModel
import com.gowthamraj07.journeytracker.ui.trips.TripsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dependencies = module {

    factory<TripsRepository> { TripsRepositoryImpl() }
    factory { GetTripsUseCase(get()) }
    viewModel {
        TripsViewModel(get())
    }

    viewModel {
        OngoingJourneyViewModel()
    }
}
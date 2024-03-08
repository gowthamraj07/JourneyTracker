package com.gowthamraj07.journeytracker

import com.gowthamraj07.journeytracker.ui.trips.TripsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dependencies = module {
    viewModel {
        TripsViewModel()
    }
}
package com.gowthamraj07.journeytracker

import androidx.room.Room
import com.gowthamraj07.journeytracker.data.TripsRepositoryImpl
import com.gowthamraj07.journeytracker.data.db.TripDatabase
import com.gowthamraj07.journeytracker.data.db.dao.TripDao
import com.gowthamraj07.journeytracker.data.flikr.FlickrApi
import com.gowthamraj07.journeytracker.data.flikr.FlickrResponseParser
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import com.gowthamraj07.journeytracker.domain.usecase.GetTripsUseCase
import com.gowthamraj07.journeytracker.ui.ongoing.journey.OngoingJourneyViewModel
import com.gowthamraj07.journeytracker.ui.trips.TripsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val dependencies = module {

    single<TripDao> {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            TripDatabase::class.java,
            "trip_database"
        ).build().tripDao()
    }

    single<FlickrApi> {
        Retrofit.Builder()
            .baseUrl("https://www.flickr.com/services/")
            .build()
            .create(FlickrApi::class.java)
    }
    factory { FlickrResponseParser() }
    factory<TripsRepository> { TripsRepositoryImpl(get(), get(), get()) }
    factory { GetTripsUseCase(get()) }
    viewModel {
        TripsViewModel(get())
    }

    viewModel {
        OngoingJourneyViewModel()
    }
}
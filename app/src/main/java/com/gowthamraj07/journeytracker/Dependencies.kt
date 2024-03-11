package com.gowthamraj07.journeytracker

import androidx.room.Room
import com.gowthamraj07.journeytracker.data.LocationRepositoryImpl
import com.gowthamraj07.journeytracker.data.PlacesRepositoryImpl
import com.gowthamraj07.journeytracker.data.TripsRepositoryImpl
import com.gowthamraj07.journeytracker.data.db.TripDatabase
import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.data.db.dao.TripDao
import com.gowthamraj07.journeytracker.data.flikr.FlickrApi
import com.gowthamraj07.journeytracker.data.flikr.FlickrResponseParser
import com.gowthamraj07.journeytracker.domain.repository.LocationRepository
import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import com.gowthamraj07.journeytracker.domain.usecase.GetTripsUseCase
import com.gowthamraj07.journeytracker.domain.usecase.LoadPlacesUseCase
import com.gowthamraj07.journeytracker.services.TripsServiceConnection
import com.gowthamraj07.journeytracker.ui.ongoing.journey.OngoingJourneyViewModel
import com.gowthamraj07.journeytracker.ui.start.journey.StartJourneyViewModel
import com.gowthamraj07.journeytracker.ui.trips.TripsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlickrApi::class.java)
    }
    factory { FlickrResponseParser() }
    factory<TripsRepository> { TripsRepositoryImpl(get(), get(), get()) }
    factory { GetTripsUseCase(get()) }
    viewModel {
        TripsViewModel(get())
    }

    single<PlaceDao> {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            TripDatabase::class.java,
            "trip_database"
        ).build().placeDao()
    }
    factory<PlacesRepository> { PlacesRepositoryImpl(get(), get(), get()) }
    factory { LoadPlacesUseCase(get()) }
    viewModel {
        OngoingJourneyViewModel(get())
    }

    single { TripsServiceConnection() }
    factory<LocationRepository> { LocationRepositoryImpl(get(), get(), get(), androidApplication()) }
    viewModel {
        StartJourneyViewModel(get())
    }
}
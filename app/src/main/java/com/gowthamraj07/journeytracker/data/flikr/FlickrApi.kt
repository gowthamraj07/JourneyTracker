package com.gowthamraj07.journeytracker.data.flikr

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query


interface FlickrApi {

    @GET("rest/?method=flickr.photos.search&api_key=de4602667b52c32d3efed4f24d46e36d&format=json&per_page=1&page=1&nojsoncallback=1")
    suspend fun getLocationDetails(@Query("lat") lat: Double,@Query("lon") lon: Double): JsonObject
}
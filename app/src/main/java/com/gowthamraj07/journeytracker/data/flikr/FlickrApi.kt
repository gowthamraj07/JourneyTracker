package com.gowthamraj07.journeytracker.data.flikr

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path


interface FlickrApi {

    @GET("rest/?method=flickr.photos.search&api_key=de4602667b52c32d3efed4f24d46e36d&lat={lat}&lon={lon}&per_page=1&page=1&format=json&nojsoncallback=1")
    suspend fun getLocationDetails(@Path("lat") lat: Double,@Path("lon") lon: Double): JsonObject
}
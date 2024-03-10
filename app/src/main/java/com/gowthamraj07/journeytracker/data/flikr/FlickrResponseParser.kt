package com.gowthamraj07.journeytracker.data.flikr

import com.google.gson.JsonObject

class FlickrResponseParser {
    fun parse(flickrResponse: JsonObject): FlickrResponse {
        val firstPhotoReference = flickrResponse.get("photos").asJsonObject.get("photo").asJsonArray.first().asJsonObject
        return FlickrResponse(
            id = firstPhotoReference.get("id").asString,
            serverId = firstPhotoReference.get("server").asString,
            secret = firstPhotoReference.get("secret").asString
        )
    }
}

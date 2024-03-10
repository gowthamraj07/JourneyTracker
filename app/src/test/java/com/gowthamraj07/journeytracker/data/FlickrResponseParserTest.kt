package com.gowthamraj07.journeytracker.data

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FlickrResponseParserTest: StringSpec({

    val parser = FlickrResponseParser()

    "should parse the json response" {
        parser.parse(
            """
                {
                    "photos": {
                        "page": 1,
                        "pages": 123861,
                        "perpage": 1,
                        "total": 123861,
                        "photo": [
                            {
                                "id": "53573430851",
                                "owner": "69506664@N06",
                                "secret": "3005ebe97a",
                                "server": "65535",
                                "farm": 66,
                                "title": "2023-11-11_22-45-07_ILCE-7C_DSCBM4637_DxO",
                                "ispublic": 1,
                                "isfriend": 0,
                                "isfamily": 0
                            }
                        ]
                    },
                    "stat": "ok"
                }
            """.trimIndent().toJsonObject()
        ) shouldBe FlickrResponse(
            id = "53573430851",
            serverId = "65535",
            secret = "3005ebe97a"
        )
    }
})

fun String.toJsonObject(): JsonObject {
    return JsonParser.parseString(this).asJsonObject
}

package com.test.toolboxmobile.data.services

import com.test.toolboxmobile.data.model.Carousel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface DataServices {
    companion object {
        private const val DATA = "v1/mobile/data"
    }

    @GET(DATA)
    fun getAsync(@HeaderMap headers: Map<String, String>): Deferred<Response<List<Carousel>>>
}
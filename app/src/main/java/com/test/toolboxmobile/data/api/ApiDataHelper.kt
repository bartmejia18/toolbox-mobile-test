package com.test.toolboxmobile.data.api

import com.test.toolboxmobile.data.model.Carousel
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface ApiDataHelper {
    suspend fun dataAsync(): Deferred<Response<List<Carousel>>>
}
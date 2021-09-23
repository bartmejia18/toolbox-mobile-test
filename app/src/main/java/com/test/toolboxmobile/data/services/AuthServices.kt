package com.test.toolboxmobile.data.services

import com.google.gson.JsonObject
import com.test.toolboxmobile.data.model.Auth
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthServices {
    companion object {
        private const val AUTH = "v1/mobile/auth"
    }

    @Headers("Content-Type: application/json")
    @POST(AUTH)
    fun authAsync(@Body data: JsonObject): Deferred<Response<Auth>>
}
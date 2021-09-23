package com.test.toolboxmobile.data.api

import com.google.gson.JsonObject
import com.test.toolboxmobile.data.model.Auth
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface ApiAuthHelper {
    suspend fun auth(sub: JsonObject): Deferred<Response<Auth>>
}
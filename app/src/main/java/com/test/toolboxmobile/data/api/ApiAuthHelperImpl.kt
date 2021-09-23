package com.test.toolboxmobile.data.api

import com.google.gson.JsonObject
import com.test.toolboxmobile.data.model.Auth
import com.test.toolboxmobile.data.services.AuthServices
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class ApiAuthHelperImpl @Inject constructor(
    private val apiServices: AuthServices
): ApiAuthHelper{
    override suspend fun auth(sub: JsonObject): Deferred<Response<Auth>> = apiServices.authAsync(sub)
}
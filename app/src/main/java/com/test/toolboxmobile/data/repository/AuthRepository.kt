package com.test.toolboxmobile.data.repository

import com.google.gson.JsonObject
import com.test.toolboxmobile.data.api.ApiAuthHelper
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiAuthHelper: ApiAuthHelper
) {
    suspend fun auth(sub: JsonObject) = apiAuthHelper.auth(sub).await()
}
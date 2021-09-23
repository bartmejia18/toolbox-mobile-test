package com.test.toolboxmobile.data.api

import com.test.toolboxmobile.data.model.Auth
import retrofit2.Response

interface ApiAuthHelper {
    suspend fun auth(sub: String): Response<Auth>
}
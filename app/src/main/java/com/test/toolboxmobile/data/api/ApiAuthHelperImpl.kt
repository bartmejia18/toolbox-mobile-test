package com.test.toolboxmobile.data.api

import com.test.toolboxmobile.data.model.Auth
import com.test.toolboxmobile.data.services.AuthServices
import retrofit2.Response
import javax.inject.Inject

class ApiAuthHelperImpl @Inject constructor(
    private val apiServices: AuthServices
): ApiAuthHelper{
    override suspend fun auth(sub: String): Response<Auth> = apiServices.authAsync(sub)
}
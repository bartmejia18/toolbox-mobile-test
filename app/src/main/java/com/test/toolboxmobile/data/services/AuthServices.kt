package com.test.toolboxmobile.data.services

import com.test.toolboxmobile.data.model.Auth
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthServices {
    companion object {
        private const val AUTH = "v1/mobile/auth"
    }

    @POST(AUTH)
    suspend fun authAsync(
        @Query("sub") sub:String
    ): Response<Auth>
}
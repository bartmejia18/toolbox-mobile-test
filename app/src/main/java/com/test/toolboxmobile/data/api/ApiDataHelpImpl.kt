package com.test.toolboxmobile.data.api

import android.content.Context
import com.example.syscredit.data.local.getFromSharedPreferences
import com.test.toolboxmobile.data.model.Carousel
import com.test.toolboxmobile.data.services.DataServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class ApiDataHelpImpl @Inject constructor(
    private val apiServices: DataServices,
    @ApplicationContext private val context: Context
): ApiDataHelper {
    override suspend fun dataAsync(): Deferred<Response<List<Carousel>>> = apiServices.getAsync(
        mapOf("Authorization" to "${context.getFromSharedPreferences("type", "")} ${context.getFromSharedPreferences("token", "")}")
    )
}
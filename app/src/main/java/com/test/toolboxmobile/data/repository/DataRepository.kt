package com.test.toolboxmobile.data.repository

import com.test.toolboxmobile.data.api.ApiDataHelper
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val apiDataHelper: ApiDataHelper
) {
    suspend fun data() = apiDataHelper.dataAsync().await()
}
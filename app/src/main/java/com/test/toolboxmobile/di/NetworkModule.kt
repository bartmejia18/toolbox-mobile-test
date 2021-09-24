package com.test.toolboxmobile.di

import com.test.toolboxmobile.core.AppConstants.BASE_URL
import com.test.toolboxmobile.core.factory.CoroutineCallAdapterFactory
import com.test.toolboxmobile.data.api.ApiAuthHelper
import com.test.toolboxmobile.data.api.ApiAuthHelperImpl
import com.test.toolboxmobile.data.api.ApiDataHelpImpl
import com.test.toolboxmobile.data.api.ApiDataHelper
import com.test.toolboxmobile.data.services.AuthServices
import com.test.toolboxmobile.data.services.DataServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client.addNetworkInterceptor(interceptor)
        return client.build()
    }

    @Provides
    fun provideAuthServices(retrofit: Retrofit): AuthServices = retrofit.create(AuthServices::class.java)

    @Provides
    fun provideAuthHelper(apiAuthHelperImpl: ApiAuthHelperImpl): ApiAuthHelper = apiAuthHelperImpl

    @Provides
    fun provideDataServices(retrofit: Retrofit): DataServices = retrofit.create(DataServices::class.java)

    @Provides
    fun ProvideDataHelper(apiDataHelpImpl: ApiDataHelpImpl): ApiDataHelper = apiDataHelpImpl
}
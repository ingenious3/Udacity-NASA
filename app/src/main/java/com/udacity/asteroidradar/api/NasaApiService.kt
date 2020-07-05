package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.constant.Constants
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofitInstanceScalars =
    Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .build()

private val retrofitInstanceMoshi =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .build()


interface NasaApiService {

    @GET("planetary/apod")
    fun getPictureOfDay(@Query("api_key") apiKey: String) : Deferred<PictureOfDay>

    @GET("neo/rest/v1/feed")
    fun getAsteroidsList(@Query("start_date") startDate: String,
                         @Query("end_date") endDate: String,
                         @Query("api_key") apiKey: String) : Deferred<String>

}

object NasaApi {

    val endpointScalar: NasaApiService by lazy {
        retrofitInstanceScalars.create(
            NasaApiService::class.java)
    }

    val endpointMoshi: NasaApiService by lazy {
        retrofitInstanceMoshi.create(
            NasaApiService::class.java)
    }
}
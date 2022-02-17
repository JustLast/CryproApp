package com.example.cryptoapp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    private const val BASE_URL = "https://min-api.cryptocompare.com/data/"
    const val BASE_IMAGE_URL = "https://cryptocompare.com/"

    private val logging = HttpLoggingInterceptor()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BASIC))
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(httpClient)
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}
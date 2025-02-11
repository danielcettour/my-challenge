package com.cettourdev.challenge.core.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://api.mercadolibre.com/"

    // interceptor para ver las llamadas okhttp en el logcat
    val interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    val client =
        OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(
                Interceptor { chain ->
                    val request: Request =
                        chain
                            .request()
                            .newBuilder()
                            .build()
                    chain.proceed(request)
                },
            ).build()

    fun getRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

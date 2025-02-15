package com.cettourdev.challenge.core.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitHelper {
    /**
     * interceptor para ver las llamadas okhttp en el logcat
     */
    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder().addInterceptor(interceptor).addNetworkInterceptor(
        Interceptor { chain ->
            val request: Request =
                chain
                    .request()
                    .newBuilder()
                    .build()
            chain.proceed(request)
        },
    ).build()
}

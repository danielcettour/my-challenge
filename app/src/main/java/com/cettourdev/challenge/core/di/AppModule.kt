package com.cettourdev.challenge.core.di

import com.cettourdev.challenge.core.network.RetrofitHelper.client
import com.cettourdev.challenge.data.network.SearchClient
import com.cettourdev.challenge.utils.Utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideSearchClient(retrofit: Retrofit): SearchClient {
        return retrofit.create(SearchClient::class.java)
    }
}
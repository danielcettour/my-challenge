package com.cettourdev.challenge.core.di

import android.content.Context
import androidx.room.Room
import com.cettourdev.challenge.core.network.RetrofitHelper.client
import com.cettourdev.challenge.data.DetailsRepository
import com.cettourdev.challenge.data.DetailsRepositoryInterface
import com.cettourdev.challenge.data.network.SearchClient
import com.cettourdev.challenge.data.roomdb.ItemDao
import com.cettourdev.challenge.data.roomdb.ItemsDatabase
import com.cettourdev.challenge.utils.Utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ItemsDatabase::class.java, "ItemsDB"
    ).build()

    @Singleton
    @Provides
    fun injectDao(database: ItemsDatabase) = database.itemDao()

    @Singleton
    @Provides
    fun injectDetailsRepository(dao: ItemDao) = DetailsRepository(dao) as DetailsRepositoryInterface

}
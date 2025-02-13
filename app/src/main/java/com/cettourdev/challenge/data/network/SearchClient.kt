package com.cettourdev.challenge.data.network

import com.cettourdev.challenge.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchClient {

    @GET("/sites/MLA/search/")
    suspend fun doSearch(@Query("q") query: String): Response<SearchResponse>

}

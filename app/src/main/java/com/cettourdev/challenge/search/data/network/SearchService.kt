package com.cettourdev.challenge.search.data.network

import com.cettourdev.challenge.core.network.RetrofitHelper
import com.cettourdev.challenge.model.ItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun doSearch(query: String): List<ItemResponse> =
        withContext(Dispatchers.IO) {
            val response = retrofit.create(SearchClient::class.java).doSearch(query)
            response.body()?.results ?: listOf()
        }
}

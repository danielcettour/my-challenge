package com.cettourdev.challenge.data.network

import com.cettourdev.challenge.model.ItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchService @Inject constructor(private val searchClient: SearchClient) {

    suspend fun doSearch(query: String): List<ItemResponse> =
        withContext(Dispatchers.IO) {
            val response = searchClient.doSearch(query)
            response.body()?.results ?: listOf()
        }
}

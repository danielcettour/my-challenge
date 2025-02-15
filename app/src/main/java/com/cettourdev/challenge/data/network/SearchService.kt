package com.cettourdev.challenge.data.network

import com.cettourdev.challenge.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class SearchService @Inject constructor(private val searchClient: SearchClient) {

    suspend fun doSearch(query: String): Response<SearchResponse> {
        return withContext(Dispatchers.IO) {
            searchClient.doSearch(query)
        }
    }
}

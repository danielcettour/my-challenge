package com.cettourdev.challenge.search.data

import com.cettourdev.challenge.model.ItemResponse
import com.cettourdev.challenge.search.data.network.SearchService

class SearchRepository {
    private val api = SearchService()

    suspend fun doSearch(query: String): List<ItemResponse> = api.doSearch(query)
}

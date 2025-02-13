package com.cettourdev.challenge.data

import com.cettourdev.challenge.model.ItemResponse
import com.cettourdev.challenge.data.network.SearchService
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: SearchService) {

    suspend fun doSearch(query: String): List<ItemResponse> = api.doSearch(query)

}

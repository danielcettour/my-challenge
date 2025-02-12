package com.cettourdev.challenge.search.domain

import com.cettourdev.challenge.model.ItemResponse
import com.cettourdev.challenge.search.data.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: SearchRepository) {

    suspend operator fun invoke(query: String): List<ItemResponse> = repository.doSearch(query)
    
}

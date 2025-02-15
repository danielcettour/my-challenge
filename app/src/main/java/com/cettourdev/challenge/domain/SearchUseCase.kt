package com.cettourdev.challenge.domain

import com.cettourdev.challenge.data.SearchRepository
import com.cettourdev.challenge.model.SearchResponse
import com.cettourdev.challenge.utils.Resource
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: SearchRepository) {

    suspend operator fun invoke(query: String): Resource<SearchResponse> = repository.doSearch(query)

}

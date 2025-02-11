package com.cettourdev.challenge.model

data class SearchResponse(
    val site_id: String,
    val query: String,
    val results: List<ItemResponse>,
)

package com.cettourdev.challenge.model

data class ItemResponse(
    val id: String,
    val title: String,
    val condition: String,
    val permalink: String,
    val thumbnail: String,
    val price: Double,
    val currency_id: String,
    val original_price: Double,
    val attributes: List<ItemAttribute>,
)

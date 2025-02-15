package com.cettourdev.challenge.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemResponse(
    @PrimaryKey(autoGenerate = false)
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

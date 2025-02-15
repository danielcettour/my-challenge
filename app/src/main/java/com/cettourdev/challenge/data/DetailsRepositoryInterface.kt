package com.cettourdev.challenge.data

import com.cettourdev.challenge.model.ItemResponse
import kotlinx.coroutines.flow.StateFlow

interface DetailsRepositoryInterface {
    suspend fun insertItem(item: ItemResponse)

    suspend fun deleteItem(item: ItemResponse)

    fun getItems(): StateFlow<List<ItemResponse>>
}
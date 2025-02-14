package com.cettourdev.challenge.domain

import com.cettourdev.challenge.data.DetailsRepository
import com.cettourdev.challenge.model.ItemResponse
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DetailsUseCase @Inject constructor(private val repository: DetailsRepository) {
    suspend fun insertItem(item: ItemResponse) {
        repository.insertItem(item)
    }

    suspend fun deleteItem(item: ItemResponse) {
        repository.deleteItem(item)
    }

    fun getItems(): StateFlow<List<ItemResponse>> {
        return repository.getItems()
    }
}
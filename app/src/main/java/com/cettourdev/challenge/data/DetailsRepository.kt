package com.cettourdev.challenge.data

import androidx.lifecycle.asFlow
import com.cettourdev.challenge.data.roomdb.ItemDao
import com.cettourdev.challenge.model.ItemResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val itemDao: ItemDao) : DetailsRepositoryInterface {
    override suspend fun insertItem(item: ItemResponse) {
        itemDao.insertItem(item)
    }

    override suspend fun deleteItem(item: ItemResponse) {
        itemDao.deleteItem(item)
    }

    /**
     * para obtener los ítems de la BD, un StateFlow tiene un mejor comportamiento que un Livedata
     */

    override fun getItems(): StateFlow<List<ItemResponse>> {
        return itemDao.observeItems()
            .asFlow() // Convertir livedata a flow
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
}
package com.cettourdev.challenge.screens.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cettourdev.challenge.domain.DetailsUseCase
import com.cettourdev.challenge.model.ItemResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsUseCase: DetailsUseCase,
) : ViewModel() {
    private val _items = MutableStateFlow<List<ItemResponse>?>(null)
    val items: StateFlow<List<ItemResponse>?> = _items

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.value = true
            detailsUseCase.getItems().collect { fetchedItems ->
                _items.value = fetchedItems
                _isLoading.value = false
            }
        }
    }

    private val _isFavourite = MutableLiveData<Boolean>(true)
    val isFavourite: LiveData<Boolean> = _isFavourite

    fun toggleFavourite(item: ItemResponse) {
        if (_isFavourite.value != true) {
            insertItem(item)
            _isFavourite.value = true
        } else {
            deleteItem(item)
            _isFavourite.value = false
        }
    }

    fun isItemFavourite(item: ItemResponse): Boolean {
        val isFab = _items.value?.any { it.id == item.id } ?: false
        _isFavourite.value = isFab
        return isFab
    }

    fun deleteItem(item: ItemResponse) = viewModelScope.launch {
        detailsUseCase.deleteItem(item)
    }

    fun insertItem(item: ItemResponse) = viewModelScope.launch {
        detailsUseCase.insertItem(item)
    }
}
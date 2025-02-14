package com.cettourdev.challenge.screens.favourites

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
class FavouritesViewModel @Inject constructor(
    private val detailsUseCase: DetailsUseCase,
) : ViewModel() {
    //private val _favouriteItemsDB = MutableStateFlow<List<ItemResponse>?>(null)
    //val favouriteItemsDB: StateFlow<List<ItemResponse>?> = _favouriteItemsDB

    private val _items = MutableLiveData<List<ItemResponse>>()
    val items: LiveData<List<ItemResponse>> = _items

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _resultsNotEmpty = MutableLiveData<Boolean>()
    val resultsNotEmpty: LiveData<Boolean> = _resultsNotEmpty

    init {
        viewModelScope.launch {
            _isLoading.value = true
            detailsUseCase.getItems().collect { fetchedItems ->
                //_favouriteItemsDB.value = fetchedItems
                _items.value = fetchedItems
                _resultsNotEmpty.value = fetchedItems.isNotEmpty()
                _isLoading.value = false
            }
        }
    }
}
package com.cettourdev.challenge.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cettourdev.challenge.model.ItemResponse
import com.cettourdev.challenge.search.domain.SearchUseCase
import kotlinx.coroutines.launch

class SearchViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val searchUseCase = SearchUseCase()

    private val _items = MutableLiveData<List<ItemResponse>>()
    val items: LiveData<List<ItemResponse>> = _items

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _resultsNotmpty = MutableLiveData<Boolean>()
    val resultsNotmpty: LiveData<Boolean> = _resultsNotmpty

    companion object {
        private const val QUERY_KEY = "search_query"
    }

    // Para mantener el valor al rotar la pantalla
    val query = savedStateHandle.getLiveData<String>(QUERY_KEY, "")

    fun onQueryChanged(newQuery: String) {
        savedStateHandle[QUERY_KEY] = newQuery
    }

    fun onSearch() {
        _items.value = listOf()
        viewModelScope.launch {
            _isLoading.value = true
            val result = query.value?.let { searchUseCase(it) }

            if (!result.isNullOrEmpty()) {
                _resultsNotmpty.value = true
                _items.value = result ?: listOf()
            } else {
                _resultsNotmpty.value = false
            }
            _isLoading.value = false
            Log.d("////", "result: $result")
        }
    }
}

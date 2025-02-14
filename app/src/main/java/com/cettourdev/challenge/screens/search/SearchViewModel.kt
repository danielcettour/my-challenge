package com.cettourdev.challenge.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cettourdev.challenge.model.ItemResponse
import com.cettourdev.challenge.domain.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
) : ViewModel() {
    private val _items = MutableLiveData<List<ItemResponse>>()
    val items: LiveData<List<ItemResponse>> = _items

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogVisible = MutableLiveData<Boolean>()
    val dialogVisible: LiveData<Boolean> = _dialogVisible

    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    private val _resultsNotEmpty = MutableLiveData<Boolean>()
    val resultsNotEmpty: LiveData<Boolean> = _resultsNotEmpty

    private val _resultsError = MutableLiveData<Boolean>()
    val resultsError: LiveData<Boolean> = _resultsError

    fun setearQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun setearDialogVisibility(visibility: Boolean) {
        _dialogVisible.value = visibility
    }

    fun onSearch() {
        _items.value = listOf()
        viewModelScope.launch {
            _isLoading.value = true
            val result = query.value?.let { searchUseCase(it) }

            if (result?.data == null && result?.message == "Error") {
                _resultsError.value = true
            } else {
                if (!result?.data?.results.isNullOrEmpty()) {
                    _resultsError.value = false
                    _resultsNotEmpty.value = true
                    _items.value = result?.data?.results ?: listOf()
                } else {
                    _resultsNotEmpty.value = false
                }
            }
            _isLoading.value = false
        }
    }
}

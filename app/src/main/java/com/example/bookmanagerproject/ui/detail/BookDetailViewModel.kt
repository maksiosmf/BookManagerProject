package com.example.bookmanagerproject.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagerproject.data.model.WorkDetail
import com.example.bookmanagerproject.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val detail: WorkDetail) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

class BookDetailViewModel(
    private val repository: BookRepository = BookRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state

    fun loadDetails(id: String) {
        viewModelScope.launch {
            try {
                _state.value = DetailUiState.Loading
                _state.value = DetailUiState.Success(
                    repository.getBookDetails(id)
                )
            } catch (e: Exception) {
                _state.value = DetailUiState.Error("Nie udało się pobrać danych")
            }
        }
    }
}
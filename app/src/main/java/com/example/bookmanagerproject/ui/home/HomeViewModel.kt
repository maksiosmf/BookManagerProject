package com.example.bookmanagerproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagerproject.data.model.Book
import com.example.bookmanagerproject.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val books: List<Book>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val repository: BookRepository = BookRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val books = repository.getBooks()
                _uiState.value = HomeUiState.Success(books)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Brak internetu albo API zdechło")
            }
        }
    }

    fun searchBooks(query: String) {
        if (query.isBlank()) {
            loadBooks()
            return
        }
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val books = repository.searchBooks(query)
                _uiState.value = HomeUiState.Success(books)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Nie udało się wyszukać książek")
            }
        }
    }
}
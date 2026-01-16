package com.example.bookmanagerproject.ui.favourites

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanagerproject.data.model.Book
import com.example.bookmanagerproject.data.preferences.FavoritesManager
import com.example.bookmanagerproject.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(context: Context) : ViewModel() {

    private val repository = BookRepository()
    private val favoritesManager = FavoritesManager(context)

    private val _favorites = MutableStateFlow<List<Book>>(emptyList())
    val favorites: StateFlow<List<Book>> = _favorites

    fun loadFavorites() {
        viewModelScope.launch {
            val ids = favoritesManager.getFavorites()
            val books = mutableListOf<Book>()
            for (id in ids) {
                try {
                    val detail = repository.getBookDetails(id)
                    books.add(
                        Book(
                            key = id,
                            title = detail.title,
                            authors = null,
                            cover_id = detail.covers?.firstOrNull()
                        )
                    )
                } catch (_: Exception) {}
            }
            _favorites.value = books
        }
    }

    fun toggleFavorite(id: String) {
        if (favoritesManager.isFavorite(id)) {
            favoritesManager.removeFavorite(id)
        } else {
            favoritesManager.addFavorite(id)
        }
        loadFavorites()
    }
}
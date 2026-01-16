package com.example.bookmanagerproject.data.preferences

import android.content.Context

class FavoritesManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    fun getFavorites(): Set<String> {
        val set = prefs.getStringSet("ids", null)
        return set?.toSet() ?: emptySet()
    }

    fun isFavorite(id: String): Boolean =
        getFavorites().contains(id)

    fun addFavorite(id: String) {
        val current = getFavorites().toMutableSet()
        current.add(id)
        prefs.edit()
            .putStringSet("ids", current)
            .apply()
    }

    fun removeFavorite(id: String) {
        val current = getFavorites().toMutableSet()
        current.remove(id)
        prefs.edit()
            .putStringSet("ids", current)
            .apply()
    }
}
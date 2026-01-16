package com.example.bookmanagerproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookmanagerproject.ui.detail.BookDetailScreen
import com.example.bookmanagerproject.ui.favourites.FavouritesScreen
import com.example.bookmanagerproject.ui.home.HomeScreen

@Composable
fun NavGraph(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { 
            HomeScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            ) 
        }
        composable("favorites") { FavouritesScreen(navController) }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            BookDetailScreen(id, navController)
        }
    }
}
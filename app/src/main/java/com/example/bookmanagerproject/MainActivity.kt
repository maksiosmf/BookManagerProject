package com.example.bookmanagerproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.bookmanagerproject.ui.navigation.NavGraph
import com.example.bookmanagerproject.ui.theme.BookManagerProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent {
                val context = LocalContext.current
                val prefs = remember { 
                    context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE) 
                }
                var isDarkTheme by remember { 
                    mutableStateOf(prefs.getBoolean("is_dark_theme", false))
                }
                
                BookManagerProjectTheme(darkTheme = isDarkTheme) {
                    NavGraph(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { newValue ->
                            isDarkTheme = newValue
                            prefs.edit().putBoolean("is_dark_theme", newValue).apply()
                        }
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
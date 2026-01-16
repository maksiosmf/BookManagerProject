package com.example.bookmanagerproject.ui.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.bookmanagerproject.data.preferences.FavoritesManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    id: String,
    navController: NavController? = null,
    viewModel: BookDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val favoritesManager = remember(context) { FavoritesManager(context) }
    var currentFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            try {
                currentFavorite = favoritesManager.isFavorite(id)
                viewModel.loadDetails(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły książki") },
                navigationIcon = {
                    IconButton(onClick = { 
                        navController?.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Wstecz"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (state) {
            is DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is DetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text((state as DetailUiState.Error).message)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadDetails(id) }) {
                            Text("Spróbuj ponownie")
                        }
                    }
                }
            }
            is DetailUiState.Success -> {
                val detail = (state as DetailUiState.Success).detail
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val coverUrl = detail.covers?.firstOrNull()?.let {
                        "https://covers.openlibrary.org/b/id/$it-L.jpg"
                    }
                    coverUrl?.let {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = null,
                            modifier = Modifier
                                .width(200.dp)
                                .height(300.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    Text(
                        detail.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    detail.first_publish_date?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Data pierwszego wydania: $it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    FavoriteButton(
                        isFavorite = currentFavorite,
                        onToggle = {
                            favoritesManager.apply {
                                if (currentFavorite) {
                                    removeFavorite(id)
                                } else {
                                    addFavorite(id)
                                }
                            }
                            currentFavorite = !currentFavorite
                        }
                    )
                    detail.description?.let { desc ->
                        Spacer(Modifier.height(16.dp))
                        when (desc) {
                            is String -> Text(
                                desc,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            is Map<*, *> -> {
                                val value = desc["value"]
                                if (value is String) {
                                    Text(
                                        value,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoriteButton(isFavorite: Boolean, onToggle: () -> Unit) {
    AnimatedContent(
        targetState = isFavorite,
        transitionSpec = {
            fadeIn(animationSpec = tween(durationMillis = 300)) with fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) { fav ->
        Button(onClick = onToggle) {
            Text(if (fav) "Usuń z ulubionych ❤️" else "Dodaj do ulubionych 🤍")
        }
    }
}
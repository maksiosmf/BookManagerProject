package com.example.bookmanagerproject.data.model

data class Book(
    val key: String,
    val title: String,
    val authors: List<Author>?,
    val cover_id: Int?
)
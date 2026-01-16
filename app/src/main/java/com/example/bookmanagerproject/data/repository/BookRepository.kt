package com.example.bookmanagerproject.data.repository

import com.example.bookmanagerproject.data.api.RetrofitClient
import com.example.bookmanagerproject.data.model.Book
import com.example.bookmanagerproject.data.model.WorkDetail

class BookRepository {

    private val api = RetrofitClient.api

    suspend fun getBooks(): List<Book> = api.getFictionBooks().works

    suspend fun searchBooks(query: String): List<Book> = api.searchBooks(query).docs

    suspend fun getBookDetails(workId: String): WorkDetail {
        val cleanId = workId.removePrefix("/works/")
        return api.getWorkDetails(cleanId)
    }
}
package com.example.bookmanagerproject.data.api

import com.example.bookmanagerproject.data.model.SearchResponse
import com.example.bookmanagerproject.data.model.SubjectResponse
import com.example.bookmanagerproject.data.model.WorkDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("subjects/fiction.json")
    suspend fun getFictionBooks(@Query("limit") limit: Int = 20): SubjectResponse

    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse

    @GET("works/{id}.json")
    suspend fun getWorkDetails(@Path("id") id: String): WorkDetail
}
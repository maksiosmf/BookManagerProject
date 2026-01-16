package com.example.bookmanagerproject.data.model

data class WorkDetail(
    val title: String,
    val description: Any?,
    val first_publish_date: String?,
    val covers: List<Int>?
)
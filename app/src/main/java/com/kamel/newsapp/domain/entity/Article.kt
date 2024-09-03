package com.kamel.newsapp.domain.entity

data class Article(
    val id: String,
    val source: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String,
    val category: String,
)
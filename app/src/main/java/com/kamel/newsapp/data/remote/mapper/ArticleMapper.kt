package com.kamel.newsapp.data.remote.mapper

import com.kamel.newsapp.data.remote.model.ArticleDto
import com.kamel.newsapp.domain.entity.Article
import com.kamel.newsapp.util.formatIsoDateToString
import java.util.UUID

fun ArticleDto.toEntity(category: String): Article {
    return Article(
        id = UUID.randomUUID().toString(),
        source = source.toEntity(),
        author = author ?: "Unknown",
        title = title ?: "N/A",
        description = description ?: "",
        url = url ?: "",
        imageUrl = urlToImage ?: "",
        publishedAt = formatIsoDateToString(publishedAt ?: ""),
        content = content ?: "N/A",
        category = category,
    )
}
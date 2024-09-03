package com.kamel.newsapp.data.local.mapper

import com.kamel.newsapp.data.local.model.ArticleCollection
import com.kamel.newsapp.domain.entity.Article
import com.kamel.newsapp.domain.entity.Source
import com.kamel.newsapp.util.formatIsoDateToString

fun ArticleCollection.toEntity(): Article {
    return Article(
        id = id,
        source = Source(id = sourceId, name = sourceName),
        author = author,
        title = title,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedAt = formatIsoDateToString(publishedAt),
        content = content,
        category = category,
    )
}

fun Article.toCollection(): ArticleCollection {
    return ArticleCollection().also {
        it.id = id
        it.sourceName = source.name
        it.sourceId = source.id
        it.author = author
        it.title = title
        it.description = description
        it.url = url
        it.urlToImage = imageUrl
        it.publishedAt = formatIsoDateToString(publishedAt)
        it.content = content
        it.category = category
    }
}
package com.kamel.newsapp.data.remote.mapper

import com.kamel.newsapp.data.remote.model.SourceDto
import com.kamel.newsapp.domain.entity.Source

fun SourceDto.toEntity(): Source {
    return Source(
        id = id,
        name = name ?: "Unknown"
    )
}
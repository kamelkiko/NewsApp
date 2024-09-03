package com.kamel.newsapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SourceDto(
    val id: String? = null,
    val name: String? = null,
)
package com.kamel.newsapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(
    val status: String,
    val totalResults: Int,
    @SerialName("articles")
    val data: T? = null,
)
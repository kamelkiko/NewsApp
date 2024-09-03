package com.kamel.newsapp.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.kamel.newsapp.presentation.screen.home.ArticleState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object NewsAppNavType {

    val ArticleType = object : NavType<ArticleState>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): ArticleState? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ArticleState {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ArticleState): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: ArticleState) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}
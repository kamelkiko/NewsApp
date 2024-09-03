package com.kamel.newsapp.data.local

import com.kamel.newsapp.data.local.model.ArticleCollection
import com.kamel.newsapp.data.repository.local.LocalDataSource
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import javax.inject.Inject

class RealmLocalStorageDataSource @Inject constructor(private val realm: Realm) : LocalDataSource {

    override suspend fun saveArticles(articles: List<ArticleCollection>) {
        realm.write {
            articles.forEach { article ->
                copyToRealm(article, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    override suspend fun deleteArticlesByCategory(category: String) {
        realm.write {
            val articleToDelete = realm.query<ArticleCollection>(
                "$CATEGORY == '$category'"
            ).find()
            articleToDelete.forEach { article ->
                val liveArticle = findLatest(article)
                if (liveArticle != null)
                    delete(liveArticle)
            }
        }
    }

    override fun getSavedArticlesByCategory(category: String): List<ArticleCollection> {
        return realm.query(
            ArticleCollection::class,
            "$CATEGORY == '$category'"
        ).find()
    }


    private companion object {
        private const val CATEGORY = "category"
    }
}
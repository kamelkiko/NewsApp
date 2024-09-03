package com.kamel.newsapp.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class ArticleCollection : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var sourceName: String = ""
    var sourceId: String? = ""
    var author: String = ""
    var title: String = ""
    var description: String = ""
    var url: String = ""
    var urlToImage: String = ""
    var publishedAt: String = ""
    var content: String = ""
    var category: String = ""
}
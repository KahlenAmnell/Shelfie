package com.bernat.shelfie.domain.model

data class Book (
    var id: String? = null,
    var title: String,
    var author: String,
    var pageCount: Int,
    var publishDate: String,
    var imageUrl: String? = null
)

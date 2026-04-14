package com.bernat.shelfie.booksScreen

import java.sql.Date

data class Book (
    var id:String? = null,
    var title: String,
    var author:String,
    var pageCount: Int,
    var publishDate: String

    )
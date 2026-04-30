package com.bernat.shelfie.domain.repository

import com.bernat.shelfie.data.model.BookResponse
import retrofit2.Response

interface BooksRepository {
    suspend fun getBookByIsbn(isbn: String, apiKey: String): Response<BookResponse>
}

package com.bernat.shelfie.data.repository

import com.bernat.shelfie.data.api.GoogleBooksService
import com.bernat.shelfie.data.model.BookResponse
import com.bernat.shelfie.domain.repository.BooksRepository
import retrofit2.Response

class NetworkBooksRepository(
    private val googleBooksService: GoogleBooksService
) : BooksRepository {
    override suspend fun getBookByIsbn(isbn: String, apiKey: String): Response<BookResponse> {
        return googleBooksService.getBookByIsbn("isbn:$isbn", apiKey)
    }
}

package com.bernat.shelfie.data.api

import com.bernat.shelfie.data.model.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {
    @GET("volumes")
    suspend fun getBookByIsbn(
        @Query("q") isbn: String
    ): Response<BookResponse>

    companion object {
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
    }
}
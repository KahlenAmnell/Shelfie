package com.bernat.shelfie.domain.repository

import com.bernat.shelfie.domain.model.Book
import com.bernat.shelfie.domain.model.ReadingStatus
import kotlinx.coroutines.flow.Flow

interface BooksDatabaseRepository {
    fun getBooks(): Flow<List<Book>>
    fun addBook(book: Book)
    fun updateBookStatus(bookId: String, status: ReadingStatus)
    fun deleteBook(bookId: String)
    fun refreshUser()
}

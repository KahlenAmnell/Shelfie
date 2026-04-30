package com.bernat.shelfie.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bernat.shelfie.data.repository.FirebaseBooksRepository
import com.bernat.shelfie.domain.model.Book
import com.bernat.shelfie.domain.model.ReadingStatus
import com.bernat.shelfie.domain.repository.BooksDatabaseRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BooksDatabaseView(
    private val repository: BooksDatabaseRepository = FirebaseBooksRepository()
) : ViewModel() {

    var listOfBooks = mutableStateListOf<Book>()
    var currentBook by mutableStateOf<Book?>(null)

    fun loadData() {
        viewModelScope.launch {
            repository.getBooks()
                .catch { e ->
                    // Handle the error gracefully to prevent crash
                    e.printStackTrace()
                    listOfBooks.clear()
                }
                .collect { books ->
                    listOfBooks.clear()
                    listOfBooks.addAll(books)
                    
                    currentBook?.let { activeBook ->
                        currentBook = books.find { it.id == activeBook.id }
                    }
                }
        }
    }

    fun onAddBook(
        title: String,
        author: String,
        pageCount: Int,
        publishDate: String,
        imageUrl: String? = null,
        status: ReadingStatus = ReadingStatus.WANT_TO_READ
    ) {
        val book = Book(
            title = title,
            author = author,
            pageCount = pageCount,
            publishDate = publishDate,
            imageUrl = imageUrl,
            status = status
        )
        repository.addBook(book)
    }

    fun onUpdateStatus(bookId: String, newStatus: ReadingStatus) {
        repository.updateBookStatus(bookId, newStatus)
    }

    fun onDeleteBook(book: Book) {
        book.id?.let { id ->
            repository.deleteBook(id)
        }
    }

    fun onLogOut() {
        listOfBooks.clear()
        currentBook = null
    }
    
    fun refreshUser() {
        repository.refreshUser()
    }
}

package com.bernat.shelfie.booksScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bernat.shelfie.BuildConfig
import com.bernat.shelfie.data.api.RetrofitClient
import com.bernat.shelfie.data.model.BookItem
import kotlinx.coroutines.launch

sealed class BookUiState {
    object Idle : BookUiState()
    object Loading : BookUiState()
    data class Success(val book: BookItem) : BookUiState()
    data class Error(val message: String) : BookUiState()
}

class BooksViewModel : ViewModel() {
    var uiState: BookUiState by mutableStateOf(BookUiState.Idle)
        private set

    fun fetchBookData(isbn: String) {
        uiState = BookUiState.Loading
        viewModelScope.launch {
            try {
                val apiKey = BuildConfig.GOOGLE_BOOKS_API_KEY
                val response = RetrofitClient.googleBooksService.getBookByIsbn("isbn:$isbn", apiKey)
                if (response.isSuccessful) {
                    val bookResponse = response.body()
                    val firstBook = bookResponse?.items?.firstOrNull()

                    if (firstBook != null) {
                        uiState = BookUiState.Success(firstBook)
                    } else {
                        uiState = BookUiState.Error("Nie znaleziono książki dla tego kodu.")
                    }
                } else {
                    uiState = BookUiState.Error("Błąd serwera: ${response.code()}")
                }
            } catch (e: Exception) {
                uiState = BookUiState.Error("Brak połączenia z internetem.")
            }
        }
    }

    fun resetState() {
        uiState = BookUiState.Idle
    }
}

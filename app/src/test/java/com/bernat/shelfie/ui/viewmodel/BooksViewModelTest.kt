package com.bernat.shelfie.ui.viewmodel

import com.bernat.shelfie.data.model.BookItem
import com.bernat.shelfie.data.model.BookResponse
import com.bernat.shelfie.data.model.VolumeInfo
import com.bernat.shelfie.domain.repository.BooksRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class BooksViewModelTest {

    private lateinit var viewModel: BooksViewModel
    private val repository: BooksRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BooksViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchBookData sets Success state when repository returns data`() = runTest {
        // Given
        val isbn = "1234567890"
        val bookItem = BookItem(id = "1", volumeInfo = mockk<VolumeInfo>(relaxed = true))
        val bookResponse = BookResponse(items = listOf(bookItem))
        
        coEvery { repository.getBookByIsbn(isbn, any()) } returns Response.success(bookResponse)

        // When
        viewModel.fetchBookData(isbn)
        
        // Then
        assertEquals(BookUiState.Loading, viewModel.uiState)
        
        advanceUntilIdle() // Czekamy na zakończenie coroutine
        
        val currentState = viewModel.uiState
        assertTrue(currentState is BookUiState.Success)
        assertEquals(bookItem, (currentState as BookUiState.Success).book)
    }

    @Test
    fun `fetchBookData sets Error state when no books found`() = runTest {
        // Given
        val isbn = "0000000000"
        val bookResponse = BookResponse(items = emptyList())
        
        coEvery { repository.getBookByIsbn(isbn, any()) } returns Response.success(bookResponse)

        // When
        viewModel.fetchBookData(isbn)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState
        assertTrue(currentState is BookUiState.Error)
        assertEquals("Nie znaleziono książki dla tego kodu.", (currentState as BookUiState.Error).message)
    }

    @Test
    fun `fetchBookData sets Error state when repository returns error`() = runTest {
        // Given
        val isbn = "111"
        coEvery { repository.getBookByIsbn(isbn, any()) } returns Response.error(404, mockk(relaxed = true))

        // When
        viewModel.fetchBookData(isbn)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState
        assertTrue(currentState is BookUiState.Error)
        assertTrue((currentState as BookUiState.Error).message.contains("Błąd serwera"))
    }
}

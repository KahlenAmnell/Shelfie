package com.bernat.shelfie.ui.viewmodel

import com.bernat.shelfie.domain.model.Book
import com.bernat.shelfie.domain.model.ReadingStatus
import com.bernat.shelfie.domain.repository.BooksDatabaseRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BooksDatabaseViewTest {

    private lateinit var viewModel: BooksDatabaseView
    private val repository: BooksDatabaseRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BooksDatabaseView(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData updates listOfBooks when repository emits new list`() = runTest {
        // Given
        val books = listOf(
            Book(id = "1", title = "Book 1", author = "Author 1", pageCount = 100, publishDate = "2021"),
            Book(id = "2", title = "Book 2", author = "Author 2", pageCount = 200, publishDate = "2022")
        )
        every { repository.getBooks() } returns flowOf(books)

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        assertEquals(2, viewModel.listOfBooks.size)
        assertEquals("Book 1", viewModel.listOfBooks[0].title)
        assertEquals("Book 2", viewModel.listOfBooks[1].title)
    }

    @Test
    fun `onAddBook calls repository addBook`() {
        // When
        viewModel.onAddBook("Title", "Author", 100, "2023")

        // Then
        verify { repository.addBook(any()) }
    }

    @Test
    fun `onUpdateStatus calls repository updateBookStatus`() {
        // When
        viewModel.onUpdateStatus("1", ReadingStatus.READING)

        // Then
        verify { repository.updateBookStatus("1", ReadingStatus.READING) }
    }

    @Test
    fun `onDeleteBook calls repository deleteBook`() {
        // Given
        val book = Book(id = "1", title = "T", author = "A", pageCount = 1, publishDate = "D")

        // When
        viewModel.onDeleteBook(book)

        // Then
        verify { repository.deleteBook("1") }
    }
}

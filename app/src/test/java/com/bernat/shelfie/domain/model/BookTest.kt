package com.bernat.shelfie.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class BookTest {

    @Test
    fun `book initialization sets properties correctly`() {
        val book = Book(
            id = "1",
            title = "Test Title",
            author = "Test Author",
            pageCount = 100,
            publishDate = "2023-01-01",
            imageUrl = "http://example.com/image.jpg",
            status = ReadingStatus.READING
        )

        assertEquals("1", book.id)
        assertEquals("Test Title", book.title)
        assertEquals("Test Author", book.author)
        assertEquals(100, book.pageCount)
        assertEquals("2023-01-01", book.publishDate)
        assertEquals("http://example.com/image.jpg", book.imageUrl)
        assertEquals(ReadingStatus.READING, book.status)
    }

    @Test
    fun `book has default status WANT_TO_READ`() {
        val book = Book(
            title = "Test Title",
            author = "Test Author",
            pageCount = 100,
            publishDate = "2023-01-01"
        )

        assertEquals(ReadingStatus.WANT_TO_READ, book.status)
    }
}

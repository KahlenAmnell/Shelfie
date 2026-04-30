package com.bernat.shelfie.data.repository

import com.bernat.shelfie.domain.model.Book
import com.bernat.shelfie.domain.model.ReadingStatus
import com.bernat.shelfie.domain.repository.BooksDatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseBooksRepository : BooksDatabaseRepository {

    private var databaseRef: DatabaseReference? = null

    init {
        refreshUser()
    }

    override fun refreshUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        databaseRef = currentUser?.let {
            FirebaseDatabase.getInstance().getReference(it.uid)
        }
    }

    override fun getBooks(): Flow<List<Book>> = callbackFlow {
        val ref = databaseRef
        if (ref == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val books = mutableListOf<Book>()
                for (ds in snapshot.children) {
                    val id = ds.key
                    val title = ds.child("title").getValue<String>()
                    val author = ds.child("author").getValue<String>()
                    val pageCount = ds.child("pageCount").getValue<Int>()
                    val publishDate = ds.child("publishDate").getValue<String>()
                    val imageUrl = ds.child("imageUrl").getValue<String>()
                    val statusString = ds.child("status").getValue<String>()

                    val status = try {
                        if (statusString != null) ReadingStatus.valueOf(statusString) else ReadingStatus.WANT_TO_READ
                    } catch (e: Exception) {
                        ReadingStatus.WANT_TO_READ
                    }

                    if (title != null && author != null && pageCount != null && publishDate != null) {
                        books.add(Book(id, title, author, pageCount, publishDate, imageUrl, status))
                    }
                }
                trySend(books)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun addBook(book: Book) {
        val key = databaseRef?.push()?.key
        if (key != null) {
            databaseRef?.child(key)?.setValue(book)
        }
    }

    override fun updateBookStatus(bookId: String, status: ReadingStatus) {
        databaseRef?.child(bookId)?.child("status")?.setValue(status.name)
    }

    override fun deleteBook(bookId: String) {
        databaseRef?.child(bookId)?.removeValue()
    }
}

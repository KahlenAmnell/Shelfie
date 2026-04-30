package com.bernat.shelfie.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bernat.shelfie.domain.model.Book
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.launch

class BooksDatabaseView: ViewModel() {

    // Używamy mutableStateListOf dla lepszej wydajności w Compose
    var listOfBooks = mutableStateListOf<Book>()

    var currentBook by mutableStateOf<Book?>(null)

    var databaseRef = Firebase.auth.currentUser?.let { Firebase.database.getReference(it.uid) }

    fun getRef(){
        databaseRef = Firebase.auth.currentUser?.let { Firebase.database.getReference(it.uid) }
    }

    val bookDatabaseListner = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            listOfBooks.clear()
            for (ds in dataSnapshot.children) {
                val id = ds.key
                val title = ds.child("title").getValue<String>()
                val author = ds.child("author").getValue<String>()
                val pageCount = ds.child("pageCount").getValue<Int>()
                val publishDate = ds.child("publishDate").getValue<String>()
                val imageUrl = ds.child("imageUrl").getValue<String>()

                if (title != null && author != null && pageCount != null && publishDate != null) {
                    listOfBooks.add(Book(id, title, author, pageCount, publishDate, imageUrl))
                }
            }
        }

        override fun onCancelled(p0: DatabaseError) {
           android.util.Log.e("Database", "Error: ${p0.message}")
        }
    }

    fun loadData(){
        viewModelScope.launch {
            databaseRef?.addValueEventListener(bookDatabaseListner)
        }
    }

    fun onAddBook(title: String, author: String, pageCount: Int, publishDate: String, imageUrl: String? = null){
        val book = Book(title = title, author = author, pageCount = pageCount, publishDate = publishDate, imageUrl = imageUrl)
        val key = databaseRef?.push()?.key
        databaseRef?.child(key!!)?.setValue(book)
    }

    fun onLogOut(){
        databaseRef?.removeEventListener(bookDatabaseListner)
        databaseRef = null
        listOfBooks.clear()
    }
}

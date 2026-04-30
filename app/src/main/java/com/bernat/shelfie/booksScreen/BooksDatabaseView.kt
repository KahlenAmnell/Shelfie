package com.bernat.shelfie.booksScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import kotlin.reflect.typeOf

class BooksDatabaseView: ViewModel() {



    var listOfBooks = mutableStateListOf<Book>()

    var currentBook by mutableStateOf<Book?>(null)

    var databaseRef = Firebase.auth.currentUser?.let { Firebase.database.getReference(it.uid) }

    fun getRef(){
        databaseRef = Firebase.auth.currentUser?.let { Firebase.database.getReference(it.uid) }
    }

    val bookDatabaseListner = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            listOfBooks.clear()

            for (ds in dataSnapshot.children) {
                val id = ds.key

                val title = ds.child("title").getValue<String>()
                val author = ds.child("author").getValue<String>()
                val pageCount = ds.child("pageCount").getValue<Int>()
                val publishDate = ds.child("publishDate").getValue<String>()

                title?.let {
                    author?.let { it1 ->
                        pageCount?.let { pageCount1 ->
                            publishDate?.let { publishDate1 ->
                                listOfBooks.add(
                                    Book(id, it, it1, pageCount1, publishDate1)
                                )
                            }
                        }
                    }
                }


            }

            println(listOfBooks)




        }

        override fun onCancelled(p0: DatabaseError) {
           println("Error: " + p0)
        }
    }

        fun  loadData(){
            viewModelScope.launch {

                databaseRef?.addValueEventListener(bookDatabaseListner)


            }

        }

    fun onAddBook(title: String, author: String, pageCount: Int, publishDate: String){
        val book = Book(title = title, author = author, pageCount = pageCount, publishDate = publishDate)
        val key = databaseRef?.push()?.key
        databaseRef?.child(key!!)?.setValue(book)



    }

    fun onLogOut(){

            databaseRef?.removeEventListener(bookDatabaseListner)

    }


}
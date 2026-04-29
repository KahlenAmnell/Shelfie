package com.bernat.shelfie.booksScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.mlkit.common.sdkinternal.model.ModelFileHelper

@Composable

fun BookDetailsScreen(booksDatabaseView: BooksDatabaseView,navController: NavController){
    val book:Book? = booksDatabaseView.currentBook
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        book?.let { Text("Title:  ${it.title}") }
        book?.let { Text("Author: ${it.author}") }
        book?.let { Text("Publish Date: ${it.publishDate}") }
        book?.let { Text("Page Count: ${it.pageCount}") }



    }

}
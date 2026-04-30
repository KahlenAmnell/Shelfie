package com.bernat.shelfie.ui.screens.books

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView

@Composable
fun BookDetailsScreen(booksDatabaseView: BooksDatabaseView, navController: NavController) {
    val book = booksDatabaseView.currentBook

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (book != null) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Autor: ${book.author}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Stron: ${book.pageCount}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Data publikacji: ${book.publishDate}",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            Text(text = "Nie wybrano książki.")
        }
    }
}

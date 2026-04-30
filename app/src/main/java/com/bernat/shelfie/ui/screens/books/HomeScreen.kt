package com.bernat.shelfie.ui.screens.books

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.domain.model.Book
import com.bernat.shelfie.domain.model.ReadingStatus
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView

@Composable
fun HomeScreen(booksDatabaseView: BooksDatabaseView, navController: NavController) {
    val books = booksDatabaseView.listOfBooks
    var bookToDelete by remember { mutableStateOf<Book?>(null) }

    if (bookToDelete != null) {
        AlertDialog(
            onDismissRequest = { bookToDelete = null },
            title = { Text("Usuń książkę") },
            text = { Text("Czy na pewno chcesz usunąć książkę \"${bookToDelete?.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        bookToDelete?.let { booksDatabaseView.onDeleteBook(it) }
                        bookToDelete = null
                    }
                ) {
                    Text("Usuń", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { bookToDelete = null }) {
                    Text("Anuluj")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, 
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (books.isEmpty()) {
            Text(
                text = "Twoja biblioteka jest pusta.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(books) { book ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .clickable {
                                booksDatabaseView.currentBook = book
                                navController.navigate(Navigation.BookDetailsScreen.route)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                AsyncImage(
                                    model = book.imageUrl,
                                    contentDescription = "Okładka książki ${book.title}",
                                    modifier = Modifier
                                        .size(60.dp, 90.dp)
                                        .padding(end = 16.dp),
                                    contentScale = ContentScale.Crop
                                )
                                // Mały wskaźnik statusu na okładce
                                StatusIndicator(
                                    status = book.status,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = (-4).dp, y = (-4).dp)
                                )
                            }
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = book.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = book.author,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = when(book.status) {
                                        ReadingStatus.WANT_TO_READ -> "Chcę przeczytać"
                                        ReadingStatus.READING -> "W trakcie"
                                        ReadingStatus.READ -> "Przeczytane"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when(book.status) {
                                        ReadingStatus.WANT_TO_READ -> MaterialTheme.colorScheme.secondary
                                        ReadingStatus.READING -> MaterialTheme.colorScheme.primary
                                        ReadingStatus.READ -> MaterialTheme.colorScheme.tertiary
                                    }
                                )
                            }
                            IconButton(onClick = { bookToDelete = book }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Usuń",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusIndicator(status: ReadingStatus, modifier: Modifier = Modifier) {
    val color = when (status) {
        ReadingStatus.WANT_TO_READ -> Color.Gray
        ReadingStatus.READING -> MaterialTheme.colorScheme.primary
        ReadingStatus.READ -> Color(0xFF4CAF50) // Zielony
    }
    
    Box(
        modifier = modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(color)
            .background(Color.White.copy(alpha = 0.2f)) // Delikatny połysk
    )
}

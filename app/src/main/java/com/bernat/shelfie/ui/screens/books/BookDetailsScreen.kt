package com.bernat.shelfie.ui.screens.books

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.domain.model.ReadingStatus
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(booksDatabaseView: BooksDatabaseView, navController: NavController) {
    val book = booksDatabaseView.currentBook
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (book != null) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = "Okładka książki ${book.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Status czytania z rozwijaną listą
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Status: ", fontWeight = FontWeight.Bold)
                    
                    Box {
                        StatusChip(
                            status = book.status,
                            onClick = { expanded = true }
                        )
                        
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            ReadingStatus.entries.forEach { status ->
                                DropdownMenuItem(
                                    text = {
                                        Text(when(status) {
                                            ReadingStatus.WANT_TO_READ -> "Chcę przeczytać"
                                            ReadingStatus.READING -> "W trakcie czytania"
                                            ReadingStatus.READ -> "Przeczytane"
                                        })
                                    },
                                    onClick = {
                                        book.id?.let { booksDatabaseView.onUpdateStatus(it, status) }
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
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

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(Navigation.EditBookScreen.route) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edytuj")
                    }

                    // Punkt 2: Implicit Intent - Otwieranie wyszukiwarki Google
                    OutlinedButton(
                        onClick = {
                            val query = "kup książkę ${book.title} ${book.author}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${Uri.encode(query)}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Kup książkę")
                    }
                }
            }
        } else {
            Text(text = "Nie wybrano książki.")
        }
    }
}

@Composable
fun StatusChip(status: ReadingStatus, onClick: () -> Unit) {
    val (text, color) = when (status) {
        ReadingStatus.WANT_TO_READ -> "Chcę przeczytać" to MaterialTheme.colorScheme.secondary
        ReadingStatus.READING -> "W trakcie czytania" to MaterialTheme.colorScheme.primary
        ReadingStatus.READ -> "Przeczytane" to MaterialTheme.colorScheme.tertiary
    }

    SuggestionChip(
        onClick = onClick,
        label = { Text(text) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            labelColor = color
        )
    )
}

package com.bernat.shelfie.ui.screens.books

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView

@Composable
fun HomeScreen(booksDatabaseView: BooksDatabaseView, navController: NavController) {
    val books = booksDatabaseView.listOfBooks

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, 
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                            .padding(16.dp)
                    ) {
                        Text(text = book.title)
                    }
                }
            }
        }
    }
}

package com.bernat.shelfie.booksScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.google.mlkit.common.sdkinternal.model.ModelFileHelper

@Composable
fun HomeScreen(booksDatabaseView: BooksDatabaseView,navController: NavController){
    var testBooks =  booksDatabaseView.listOfBooks

    println(testBooks)
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn() {
            items(testBooks) { book ->
                Card(Modifier.fillMaxSize().padding(5.dp).clickable(true, onClick = {booksDatabaseView.currentBook = book ; navController.navigate(
                    Navigation.BookDetailsScreen.route) })) {
                    Row(Modifier.fillMaxSize().padding(5.dp)) {

                        Text("${book.title}")


                    }
                }
            }
        }
    }
}
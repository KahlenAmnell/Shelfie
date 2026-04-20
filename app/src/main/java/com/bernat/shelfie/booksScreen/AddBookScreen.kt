package com.bernat.shelfie.booksScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation

@Composable
fun AddBookScreen(
    navController: NavController,
    booksDatabaseView: BooksDatabaseView,
    initialIsbn: String = "",
    initialTitle: String = "",
    initialAuthor: String = "",
    initialYear: String = "",
    initialPages: String = ""
) {
    val context = LocalContext.current
    var titleText by remember(initialTitle) { mutableStateOf(initialTitle) }
    var authorText by remember(initialAuthor) { mutableStateOf(initialAuthor) }
    var yearText by remember(initialYear) { mutableStateOf(initialYear) }
    var pageCount by remember(initialPages) { mutableStateOf(initialPages) }
    var isbnText by remember(initialIsbn) { mutableStateOf(initialIsbn) }

    fun clear(){
        titleText = ""
        authorText = ""
        yearText = ""
        pageCount = ""
        isbnText = ""
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Row() {
         TextField(titleText, { titleText = it }, label = { Text("title") })
        }
        Row() {
            TextField(authorText, { authorText = it }, label = { Text("autor") })
        }
        Row() {
            TextField(yearText, { yearText = it }, label = { Text("rok wydania") })
        }
        Row() {
            TextField(pageCount, { pageCount = it }, label = { Text("ilosc stron") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        }
        Row() {
            TextField(isbnText, { isbnText = it }, label = { Text("isbn") })
        }
        Row() {
            Button({
                if (titleText.isNotBlank()) {
                    val addedTitle = titleText
                    booksDatabaseView.onAddBook(titleText, authorText, pageCount.toIntOrNull() ?: 0, yearText)
                    Toast.makeText(context, "Dodano książkę: $addedTitle", Toast.LENGTH_SHORT).show()
                    clear()
                } else {
                    Toast.makeText(context, "Nazwa książki jest obowiązkowa", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("add book")
            }
            Button({navController.navigate(Navigation.AddWithIsbnScreen.route)}) {
                Text("add with isbn")
            }

        }
    }
}
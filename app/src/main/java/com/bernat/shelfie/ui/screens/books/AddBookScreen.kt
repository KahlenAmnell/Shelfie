package com.bernat.shelfie.ui.screens.books

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView

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

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = titleText,
            onValueChange = { titleText = it },
            label = { Text("Tytuł") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        TextField(
            value = authorText,
            onValueChange = { authorText = it },
            label = { Text("Autor") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        TextField(
            value = yearText,
            onValueChange = { yearText = it },
            label = { Text("Rok wydania") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        TextField(
            value = pageCount,
            onValueChange = { pageCount = it },
            label = { Text("Ilość stron") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        TextField(
            value = isbnText,
            onValueChange = { isbnText = it },
            label = { Text("ISBN") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (titleText.isNotBlank()) {
                    val addedTitle = titleText
                    booksDatabaseView.onAddBook(titleText, authorText, pageCount.toIntOrNull() ?: 0, yearText)
                    Toast.makeText(context, "Dodano książkę: $addedTitle", Toast.LENGTH_SHORT).show()
                    clear()
                    navController.navigate(Navigation.HomeScreen.route) {
                        popUpTo(Navigation.HomeScreen.route) { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Nazwa książki jest obowiązkowa", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Dodaj książkę")
            }
            Button(onClick = { navController.navigate(Navigation.AddWithIsbnScreen.route) }) {
                Text("Dodaj przez ISBN")
            }
        }
    }
}

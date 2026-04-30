package com.bernat.shelfie.ui.screens.books

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.domain.model.ReadingStatus
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    navController: NavController,
    booksDatabaseView: BooksDatabaseView,
    initialIsbn: String = "",
    initialTitle: String = "",
    initialAuthor: String = "",
    initialYear: String = "",
    initialPages: String = "",
    initialImageUrl: String = ""
) {
    val context = LocalContext.current
    var titleText by remember(initialTitle) { mutableStateOf(initialTitle) }
    var authorText by remember(initialAuthor) { mutableStateOf(initialAuthor) }
    var yearText by remember(initialYear) { mutableStateOf(initialYear) }
    var pageCount by remember(initialPages) { mutableStateOf(initialPages) }
    var isbnText by remember(initialIsbn) { mutableStateOf(initialIsbn) }
    var imageUrlText by remember(initialImageUrl) { mutableStateOf(initialImageUrl) }
    
    // Stan statusu czytania
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(ReadingStatus.WANT_TO_READ) }

    fun clear(){
        titleText = ""
        authorText = ""
        yearText = ""
        pageCount = ""
        isbnText = ""
        imageUrlText = ""
        selectedStatus = ReadingStatus.WANT_TO_READ
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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

        Spacer(modifier = Modifier.height(8.dp))

        // Wybór statusu
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            TextField(
                value = when(selectedStatus) {
                    ReadingStatus.WANT_TO_READ -> "Chcę przeczytać"
                    ReadingStatus.READING -> "W trakcie czytania"
                    ReadingStatus.READ -> "Przeczytane"
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Status czytania") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
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
                            selectedStatus = status
                            expanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (titleText.isNotBlank()) {
                    val addedTitle = titleText
                    booksDatabaseView.onAddBook(
                        titleText, 
                        authorText, 
                        pageCount.toIntOrNull() ?: 0, 
                        yearText,
                        imageUrlText.ifBlank { null },
                        selectedStatus
                    )
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

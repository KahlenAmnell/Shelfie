package com.bernat.shelfie.ui.screens.books

import android.R.attr.top
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.domain.model.Book
import com.bernat.shelfie.domain.model.ReadingStatus
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookScreen(
    navController: NavController,
    booksDatabaseView: BooksDatabaseView,
) {
    val context = LocalContext.current
    val bookToEdit = booksDatabaseView.currentBook
    var titleText by remember(bookToEdit?.title) { mutableStateOf(bookToEdit?.title  ) }
    var authorText by remember(bookToEdit?.author) { mutableStateOf(bookToEdit?.author ) }
    var yearText by remember(bookToEdit?.publishDate) { mutableStateOf(bookToEdit?.publishDate) }
    var pageCount by remember(bookToEdit?.pageCount.toString()) { mutableStateOf(bookToEdit?.pageCount.toString() ) }


    // Stan statusu czytania
    var expanded by remember { mutableStateOf(false) }


    fun clear(){
        titleText = ""
        authorText = ""
        yearText = ""
        pageCount = ""

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        titleText?.let {
            TextField(
                value = it,
                onValueChange = { titleText = it },
                label = { Text("Tytuł") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }
        authorText?.let {
            TextField(
                value = it,
                onValueChange = { authorText = it },
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }
        TextField(
            value = yearText.toString(),
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




        Button({
            val newBook:Book? = titleText?.let { authorText?.let { author -> yearText?.let { publishDate ->
                bookToEdit?.let { it1 ->
                    Book(bookToEdit?.id,it,author,pageCount.toInt(),publishDate,
                        bookToEdit?.imageUrl,it1.status)
                }
            } }
            }
            newBook?.let { bookToEdit?.id?.let { bookId -> booksDatabaseView.onUpadteBook(bookId,it) } }
            navController.navigate(Navigation.HomeScreen.route)
        }) {
            Text("Edytuj książkę")
        }


    }
}
package com.bernat.shelfie.booksScreen

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation

@Composable
fun AddBookScreen(navController: NavController,booksDatabaseView: BooksDatabaseView){
    var titleText by remember { mutableStateOf("") }
    var authorText by remember { mutableStateOf("") }
    var yearText by remember { mutableStateOf("") }
    var pageCount by remember { mutableStateOf("") }

    fun clear(){
        titleText = ""
        authorText = ""
        yearText = ""
        pageCount = ""
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
            Button({
                booksDatabaseView.onAddBook(titleText,authorText,pageCount.toInt(), yearText)
                clear()
            }) {
                Text("add book")
            }
            Button({navController.navigate(Navigation.AddWithIsbnScreen.route)}) {
                Text("add with isbn")
            }

        }
    }


}
package com.bernat.shelfie.booksScreen

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.ui.scanner.CameraPreview

@Composable
fun AddWithIsbnScreen(
    navController: NavController,
    booksViewModel: BooksViewModel = viewModel()
) {
    var isbnText by remember { mutableStateOf("") }
    var isScannerVisible by remember { mutableStateOf(false) }
    var isProcessingScan by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val uiState = booksViewModel.uiState

    LaunchedEffect(uiState) {
        when (uiState) {
            is BookUiState.Success -> {
                val info = uiState.book.volumeInfo
                val fetchedTitle = info.title ?: ""
                val fetchedAuthor = info.authors?.joinToString(", ") ?: ""
                val fetchedIsbn = isbnText
                val fetchedYear = info.publishedDate?.take(4) ?: ""
                val fetchedPages = info.pageCount?.toString() ?: ""

                navController.navigate(
                    Navigation.AddBookScreen.createRoute(
                        isbn = fetchedIsbn,
                        title = fetchedTitle,
                        author = fetchedAuthor,
                        year = fetchedYear,
                        pages = fetchedPages
                    )
                ) {
                    popUpTo(Navigation.AddWithIsbnScreen.route) { inclusive = true }
                }
                booksViewModel.resetState()
                isProcessingScan = false
            }
            is BookUiState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
                isProcessingScan = false
            }
            else -> {}
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isScannerVisible = true
        } else {
            Log.d("PERMISSION", "Camera permission denied")
        }
    }

    if (isScannerVisible) {
        Box(modifier = Modifier.fillMaxSize()) {
            ScannerScreen(onIsbnScanned = { scannedIsbn ->
                if (!isProcessingScan) {
                    isProcessingScan = true
                    isbnText = scannedIsbn
                    isScannerVisible = false
                    Toast.makeText(context, "Kod wykryty: $scannedIsbn", Toast.LENGTH_SHORT).show()
                    booksViewModel.fetchBookData(scannedIsbn)
                }
            })

            Button(
                onClick = { isScannerVisible = false },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Text("Cancel")
            }
        }
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState is BookUiState.Loading) {
                CircularProgressIndicator()
            }

            TextField(
                value = isbnText,
                onValueChange = { isbnText = it },
                label = { Text("ISBN") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.padding(top = 16.dp)) {
                Button(onClick = {
                    if (isbnText.isNotBlank()) {
                        booksViewModel.fetchBookData(isbnText)
                    } else {
                        Toast.makeText(context, "Wpisz kod ISBN", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Pobierz dane")
                }

                Button(
                    onClick = {
                        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            isScannerVisible = true
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Scan ISBN")
                }
            }
        }
    }
}

@Composable
fun ScannerScreen(onIsbnScanned: (String) -> Unit) {
    CameraPreview(onBarcodeDetected = { isbn ->
        Log.d("SCANNER", "Wykryto ISBN: $isbn")
        onIsbnScanned(isbn)
    })
}
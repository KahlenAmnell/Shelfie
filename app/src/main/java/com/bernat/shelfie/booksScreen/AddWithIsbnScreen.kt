package com.bernat.shelfie.booksScreen

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bernat.shelfie.ui.scanner.CameraPreview

@Composable
fun AddWithIsbnScreen() {
    var isbnText by remember { mutableStateOf("") }
    var isScannerVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                isbnText = scannedIsbn
                isScannerVisible = false
            })

            Button(
                onClick = { isScannerVisible = false },
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
            ) {
                Text("Cancel")
            }
        }
    } else {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                TextField(
                    value = isbnText,
                    onValueChange = { isbnText = it },
                    label = { Text("ISBN") }
                )
            }

            Row(modifier = Modifier.padding(top = 16.dp)) {
                Button(onClick = { /* Tutaj logika zapisu do bazy */ }) {
                    Text("Add Book")
                }

                Button(
                    onClick = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                isScannerVisible = true
                            }
                            else -> {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
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
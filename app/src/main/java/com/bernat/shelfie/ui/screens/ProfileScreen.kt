package com.bernat.shelfie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.R
import com.bernat.shelfie.ui.viewmodel.AccountViewModel
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfileScreen(navController: NavController, accountViewModel: AccountViewModel, booksDatabaseView: BooksDatabaseView) {
    val userEmail = remember { Firebase.auth.currentUser?.email ?: "Użytkownik" }
    val name = userEmail.substringBefore("@")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                
                Text(
                    text = "Witaj, $name!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button(
                    onClick = {
                        // 1. Najpierw nawiguj
                        navController.navigate(Navigation.StartScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                        
                        // 2. Potem wyloguj
                        booksDatabaseView.onLogOut()
                        accountViewModel.onLogout()
                    },
                    modifier = Modifier.size(width = 200.dp, height = 50.dp)
                ) {
                    Text("Wyloguj się", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

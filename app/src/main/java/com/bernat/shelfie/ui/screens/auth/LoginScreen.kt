package com.bernat.shelfie.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.ui.viewmodel.AccountViewModel

@Composable
fun LoginScreen(navController: NavController, accountViewModel: AccountViewModel) {
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    // Słuchamy błędów z ViewModelu i wyświetlamy Toast
    LaunchedEffect(Unit) {
        accountViewModel.authError.collect { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Witaj w Shelfie",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "Zaloguj się, aby zarządzać swoją biblioteką",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email Field - używamy stanu z ViewModelu, by zachować maila po błędzie
            OutlinedTextField(
                value = accountViewModel.email,
                onValueChange = { accountViewModel.email = it },
                label = { Text("E-mail") },
                placeholder = { Text("twoj@email.com") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field z opcją pokazywania hasła
            OutlinedTextField(
                value = accountViewModel.password,
                onValueChange = { accountViewModel.password = it },
                label = { Text("Hasło") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    val description = if (passwordVisible) "Ukryj hasło" else "Pokaż hasło"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = {
                    if (accountViewModel.email.isBlank() || accountViewModel.password.isBlank()) {
                        Toast.makeText(context, "Uzupełnij wymagane dane", Toast.LENGTH_SHORT).show()
                    } else {
                        // Przekazujemy dane z pól do funkcji logowania
                        accountViewModel.onLogin(accountViewModel.email, accountViewModel.password)
                        navController.navigate(Navigation.LoginLoadingScreen.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Zaloguj się", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Suggestion
            TextButton(
                onClick = { navController.navigate(Navigation.RegisterScreen.route) }
            ) {
                Text(
                    text = "Nie masz konta? Zarejestruj się",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

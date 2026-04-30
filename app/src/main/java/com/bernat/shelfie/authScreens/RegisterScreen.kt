package com.bernat.shelfie.authScreens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation

@Composable
fun RegisterScreen(navController: NavController, accountViewModel: AccountViewModel) {
    var emailTextFieldText by remember { mutableStateOf("") }
    var passwordextFieldText by remember { mutableStateOf("") }
    var comfirmPsswordextFieldText by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun clear() {
        emailTextFieldText = ""
        passwordextFieldText = ""
        comfirmPsswordextFieldText = ""
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Dołącz do Shelfie",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "Stwórz konto, aby zacząć kolekcjonować książki",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email Field
            OutlinedTextField(
                value = emailTextFieldText,
                onValueChange = { emailTextFieldText = it },
                label = { Text("E-mail") },
                placeholder = { Text("twoj@email.com") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = passwordextFieldText,
                onValueChange = { passwordextFieldText = it },
                label = { Text("Hasło") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = comfirmPsswordextFieldText,
                onValueChange = { comfirmPsswordextFieldText = it },
                label = { Text("Potwierdź hasło") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            Button(
                onClick = {
                    if (passwordextFieldText != comfirmPsswordextFieldText) {
                        Toast.makeText(context, "Hasła są różne", Toast.LENGTH_SHORT).show()
                        // Usunąłem czyszczenie pól tutaj, aby użytkownik mógł poprawić błąd bez wpisywania wszystkiego od nowa
                    } else if (passwordextFieldText == "" || emailTextFieldText == "") {
                        Toast.makeText(context, "Proszę wprowadzić wszystkie wymagane dane", Toast.LENGTH_SHORT).show()
                    } else if (!(emailTextFieldText.contains("@") && emailTextFieldText.contains("."))) {
                        Toast.makeText(context, "Proszę wprowadzić prawidłowy email", Toast.LENGTH_SHORT).show()
                    } else if (passwordextFieldText.length < 8) {
                        Toast.makeText(context, "Hasło musi mieć co najmniej 8 znaków", Toast.LENGTH_SHORT).show()
                    } else if (!(passwordextFieldText.any { it.isUpperCase() } && passwordextFieldText.any { it.isDigit() })) {
                        Toast.makeText(context, "Hasło musi zawierać duże litery oraz cyfry", Toast.LENGTH_SHORT).show()
                    } else {
                        accountViewModel.onRegister(emailTextFieldText, passwordextFieldText)
                        navController.navigate(Navigation.LoginScreen.route)
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
                Text("Zarejestruj się", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Suggestion
            TextButton(
                onClick = { navController.navigate(Navigation.LoginScreen.route) }
            ) {
                Text(
                    text = "Masz już konto? Zaloguj się",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

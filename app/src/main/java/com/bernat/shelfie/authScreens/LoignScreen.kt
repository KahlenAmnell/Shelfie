package com.bernat.shelfie.authScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.core.os.registerForAllProfilingResults
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import okhttp3.internal.notify
import okhttp3.internal.wait

@Composable
fun LoginScreen(navController: NavController,accountViewModel: AccountViewModel){
    var emailTextFieldText by remember{mutableStateOf("")}
    var passwordextFieldText by remember{mutableStateOf("")}
    val context = LocalContext.current

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        TextField(emailTextFieldText,{emailTextFieldText=it}, label = {Text("email")})
        TextField(passwordextFieldText,{passwordextFieldText=it}, label = {Text("password")}, visualTransformation = PasswordVisualTransformation())
        Row() {
            Button({


                     if (emailTextFieldText == "" || passwordextFieldText == "") {
                         Toast.makeText(context, "Uzupełnij wymagane dane", Toast.LENGTH_SHORT)
                             .show()
                     } else {
                         accountViewModel.onLogin(emailTextFieldText,passwordextFieldText)
                         navController.navigate(Navigation.LoginLoadingScreen.route)

                 }
            }) {
                Text("Login")
            }
            Button({navController.navigate(Navigation.RegisterScreen.route)}) {
                Text("Register")
            }
        }
    }
}
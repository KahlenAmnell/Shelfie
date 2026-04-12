package com.bernat.shelfie

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
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController,accountViewModel: AccountViewModel) {
    var emailTextFieldText by remember { mutableStateOf("") }
    var passwordextFieldText by remember { mutableStateOf("") }
    var comfirmPsswordextFieldText by remember { mutableStateOf("") }
    var context = LocalContext.current

    fun clear(){
        emailTextFieldText = ""
        passwordextFieldText = ""
        comfirmPsswordextFieldText =""
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(emailTextFieldText, { emailTextFieldText = it }, label = { Text("email") })
        TextField(
            passwordextFieldText,
            { passwordextFieldText = it },
            label = { Text("password") },
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            comfirmPsswordextFieldText,
            { comfirmPsswordextFieldText = it },
            label = { Text("comfirm password") },
            visualTransformation = PasswordVisualTransformation()
        )


            Button({
                if(passwordextFieldText == comfirmPsswordextFieldText){
                    accountViewModel.onRegister(emailTextFieldText,passwordextFieldText)
                    navController.navigate(Navigation.StartScreen.route)

                }else{
                    Toast.makeText(context,"Hasla sa rozne", Toast.LENGTH_SHORT).show()
                    clear()

                }
            }) {
                Text("Register")
            }


    }
}
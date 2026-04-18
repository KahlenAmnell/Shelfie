package com.bernat.shelfie.authScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.bernat.shelfie.Navigation

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
                if(passwordextFieldText != comfirmPsswordextFieldText ){
                    Toast.makeText(context,"Hasla sa rozne", Toast.LENGTH_SHORT).show()
                    clear()

                }else if(passwordextFieldText =="" || emailTextFieldText == ""){
                    Toast.makeText(context,"Prosze wprowadzić wszystkie wymagane dane", Toast.LENGTH_SHORT).show()
                }else if(!(emailTextFieldText.contains("@")&& emailTextFieldText.contains("."))){
                    Toast.makeText(context,"Prosze wprowadzić prawidłowy email", Toast.LENGTH_SHORT).show()
                }else if(passwordextFieldText.length < 8){
                    Toast.makeText(context,"Hasło musi miec co najmniej 8 znaków", Toast.LENGTH_SHORT).show()
                }
                else if(!(passwordextFieldText.contains(regex = Regex(pattern = "[A-Z]*")) && passwordextFieldText.contains(regex = Regex(pattern = "[0-9]*")))){
                    Toast.makeText(context,"Hasło musi zawierać duże litery oraz cyfry", Toast.LENGTH_SHORT).show()
                }
                else{

                    accountViewModel.onRegister(emailTextFieldText,passwordextFieldText)
                    navController.navigate(Navigation.LoginScreen.route)

                }
            }) {
                Text("Register")
            }


    }
}
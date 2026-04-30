package com.bernat.shelfie.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AccountViewModel: ViewModel() {

    // Stan dla logowania
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Stan dla rejestracji
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    private val _authError = MutableSharedFlow<String>()
    val authError = _authError.asSharedFlow()

    fun onLogin(emailToLogin: String, passwordToLogin: String) {
        viewModelScope.launch {
            try {
                Firebase.auth.signInWithEmailAndPassword(emailToLogin, passwordToLogin).await()
                Log.d("AUTH", "Zalogowano pomyślnie")
            } catch (e: Exception) {
                Log.e("AUTH", "Błąd logowania: ${e.message}")
                _authError.emit("Nieprawidłowe dane logowania lub błąd połączenia")
            }
        }
    }

    fun onRegister(emailToRegister: String, passwordToRegister: String) {
        viewModelScope.launch {
            try {
                Firebase.auth.createUserWithEmailAndPassword(emailToRegister, passwordToRegister).await()
                Firebase.auth.signOut()
                Log.d("AUTH", "Zarejestrowano pomyślnie")
                // Czyścimy pola rejestracji po sukcesie
                clearRegisterFields()
            } catch (e: Exception) {
                Log.e("AUTH", "Błąd rejestracji: ${e.message}")
                _authError.emit("Błąd rejestracji: ${e.localizedMessage}")
            }
        }
    }

    fun onLogout() {
        Firebase.auth.signOut()
        email = ""
        password = ""
    }

    private fun clearRegisterFields() {
        registerEmail = ""
        registerPassword = ""
        confirmPassword = ""
    }
}

package com.bernat.shelfie.authScreens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AccountViewModel: ViewModel() {

    fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                Firebase.auth.signInWithEmailAndPassword(email, password).await()
                Log.d("AUTH", "Zalogowano pomyślnie")
            } catch (e: Exception) {
                Log.e("AUTH", "Błąd logowania: ${e.message}")
            }
        }
    }

    fun onRegister(email: String, password: String) {
        viewModelScope.launch {
            try {
                Firebase.auth.createUserWithEmailAndPassword(email, password).await()
                Firebase.auth.signOut()
                Log.d("AUTH", "Zarejestrowano pomyślnie")
            } catch (e: Exception) {
                Log.e("AUTH", "Błąd rejestracji: ${e.message}")
            }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            Firebase.auth.signOut()
        }
    }
}

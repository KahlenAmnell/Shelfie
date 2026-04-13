package com.bernat.shelfie.authScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel() {



    fun onLogin(email: String,password: String){
        viewModelScope.launch {
            Firebase.auth.signInWithEmailAndPassword(email, password)
        }
    }
    fun onRegister(email: String,password: String){
        viewModelScope.launch {
            Firebase.auth.createUserWithEmailAndPassword(email, password)
            Firebase.auth.signOut()
        }
    }

    fun onLogout(){
        viewModelScope.launch {
            Firebase.auth.signOut()
        }
    }
}
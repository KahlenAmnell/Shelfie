package com.bernat.shelfie.authScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.R
import com.bernat.shelfie.booksScreen.BooksDatabaseView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import okhttp3.internal.wait

@Composable
fun LoginLoadingScreen(navController: NavController,booksDatabaseView: BooksDatabaseView,accountViewModel: AccountViewModel){
    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val imagePainter = painterResource(R.drawable.ic_launcher_foreground)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(imagePainter,null, Modifier.size(240.dp))
                Text(
                    text = "It's me, Shelfie!",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )

                )

            }
        }
    }


    LaunchedEffect(Lifecycle.Event.ON_RESUME) {


        delay(2000)

        if(Firebase.auth.currentUser != null) {

            booksDatabaseView.getRef()
            booksDatabaseView.loadData()
            delay(2000)

            Toast.makeText(context, "Logowanie się powiodło", Toast.LENGTH_SHORT).show()
            navController.navigate(Navigation.HomeScreen.route)


        }else{
            delay(2000)
            navController.navigate(Navigation.LoginScreen.route)
            Toast.makeText(context,"Logowanie się nie powiodło", Toast.LENGTH_SHORT).show()
        }


    }
}
package com.bernat.shelfie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bernat.shelfie.authScreens.AccountViewModel
import com.bernat.shelfie.authScreens.LoginScreen
import com.bernat.shelfie.authScreens.RegisterScreen
import com.bernat.shelfie.ui.theme.ShelfieTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


sealed class Navigation(val route:String){
        object StartScreen: Navigation(route = "startScreen")
        object LoginScreen: Navigation(route = "loginScreen")
        object RegiserScreen: Navigation(route = "registerScreen")

        object AddBookScreen: Navigation(route = "addBookScreen")
        object HomeScreen: Navigation(route = "HomeScreen")
}





class MainActivity : ComponentActivity() {

    private val accountViewModel by viewModels<AccountViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AccountViewModel() as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShelfieTheme {
                Surface(Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavController(navController,accountViewModel)
                }
            }
        }
    }
}



@Composable
fun NavController(navController: NavHostController,accountViewModel: AccountViewModel){
    Scaffold(
        bottomBar = {
            NavigationBar() {
                NavigationBarItem(false,{navController.navigate(Navigation.StartScreen.route)}, icon = {Text("Profile")})
                NavigationBarItem(false,{navController.navigate(Navigation.LoginScreen.route)}, icon = {Text("Login")})
            }
        }

    ) { innerPading->
        NavHost(navController, startDestination = Navigation.StartScreen.route, Modifier.padding(innerPading)) {
            composable(Navigation.StartScreen.route) {
                if (Firebase.auth.currentUser == null) {
                    StartScreen(navController)
                } else {
                    ProfileScreen(navController, accountViewModel)
                }
            }
            composable(Navigation.LoginScreen.route) {
                LoginScreen(
                    navController,
                    accountViewModel
                )
            }
            composable(Navigation.RegiserScreen.route) {
                RegisterScreen(
                    navController,
                    accountViewModel
                )
            }
        }
    }
}


@Composable
fun StartScreen(navController: NavController){
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
                Button({navController.navigate(Navigation.LoginScreen.route)}) {
                    Text(
                        text = "Login",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                    )
                }

            }
        }
    }
}
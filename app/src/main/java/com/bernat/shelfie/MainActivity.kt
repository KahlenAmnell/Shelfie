package com.bernat.shelfie

import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bernat.shelfie.authScreens.AccountViewModel
import com.bernat.shelfie.authScreens.LoginLoadingScreen
import com.bernat.shelfie.authScreens.LoginScreen
import com.bernat.shelfie.authScreens.RegisterScreen
import com.bernat.shelfie.booksScreen.AddBookScreen
import com.bernat.shelfie.booksScreen.AddWithIsbnScreen
import com.bernat.shelfie.booksScreen.HomeScreen
import com.bernat.shelfie.ui.theme.ShelfieTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


sealed class Navigation(val route:String){
        object StartScreen: Navigation(route = "startScreen")
        object LoginScreen: Navigation(route = "loginScreen")
        object RegiserScreen: Navigation(route = "registerScreen")

        object AddBookScreen: Navigation(route = "addBookScreen")
        object HomeScreen: Navigation(route = "homeScreen")
        object LoginLoadingScreen: Navigation(route = "loginLoadnigScreen")
        object AddWithIsbnScreen: Navigation(route = "addWithIsbnScreen")


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





                                BottomNavigation(
                                    navController,
                                    accountViewModel,
                                    Navigation.StartScreen.route
                                )



                }
            }
        }
    }
}


@Composable
fun BottomNavigation(navController: NavHostController,accountViewModel: AccountViewModel,startDestination: String){
    Scaffold(
        bottomBar = {
            NavigationBar() {

                    NavigationBarItem(
                        false,
                        { navController.navigate(Navigation.HomeScreen.route) },
                        icon = { Text("Home") })
                    NavigationBarItem(
                        false,
                        { navController.navigate(Navigation.AddBookScreen.route) },
                        icon = { Text("Add Book") })
                    NavigationBarItem(
                        false,
                        { navController.navigate(Navigation.LoginScreen.route) },
                        icon = { Text("Profile") })



            }
        }

    ) { innerPading->
        NavController(navController,accountViewModel, Modifier.padding(innerPading),startDestination)

    }
}
@Composable
fun NavController(navController: NavHostController,accountViewModel: AccountViewModel,modifier: Modifier,startDestination: String){

    NavHost(navController, startDestination = startDestination,modifier=modifier) {
        composable(Navigation.StartScreen.route) {
            if (Firebase.auth.currentUser == null) {
                StartScreen(navController)
            } else {
                ProfileScreen(navController, accountViewModel)
            }
        }
        composable(Navigation.LoginScreen.route) {
            if(Firebase.auth.currentUser == null) {
                LoginScreen(
                    navController,
                    accountViewModel
                )
            }else{
                ProfileScreen(navController,accountViewModel)
            }

        }
        composable(Navigation.RegiserScreen.route) {
            RegisterScreen(
                navController,
                accountViewModel
            )
        }
        composable(Navigation.LoginLoadingScreen.route) {
            LoginLoadingScreen(navController)
        }
        composable(Navigation.HomeScreen.route) {
            if (Firebase.auth.currentUser == null) {
            LoginScreen(navController,accountViewModel)
            } else {
            HomeScreen()
            }
            }
        composable(Navigation.AddBookScreen.route) {
            if (Firebase.auth.currentUser == null) {
                LoginScreen(navController,accountViewModel)
            } else {
                AddBookScreen(navController)
            }

        }
        composable(Navigation.AddWithIsbnScreen.route) { AddWithIsbnScreen() }

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
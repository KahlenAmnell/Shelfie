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
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bernat.shelfie.authScreens.AccountViewModel
import com.bernat.shelfie.authScreens.LoginLoadingScreen
import com.bernat.shelfie.authScreens.LoginScreen
import com.bernat.shelfie.authScreens.RegisterScreen
import com.bernat.shelfie.booksScreen.AddBookScreen
import com.bernat.shelfie.booksScreen.AddWithIsbnScreen
import com.bernat.shelfie.booksScreen.BookDetailsScreen
import com.bernat.shelfie.booksScreen.BooksDatabaseView
import com.bernat.shelfie.booksScreen.HomeScreen
import com.bernat.shelfie.ui.screens.ProfileScreen
import com.bernat.shelfie.ui.theme.ShelfieTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


sealed class Navigation(val route: String) {
    object StartScreen : Navigation("startScreen")
    object LoginScreen : Navigation("loginScreen")
    object RegisterScreen : Navigation("registerScreen")
    object HomeScreen : Navigation("homeScreen")
    object LoginLoadingScreen : Navigation("loginLoadingScreen")
    object AddWithIsbnScreen : Navigation("addWithIsbnScreen")

    object BookDetailsScreen : Navigation("bookDetailsScreen")
    object ProfileScreen : Navigation("profileScreen")

    // Trasa z opcjonalnymi argumentami
    object AddBookScreen : Navigation("addBookScreen?isbn={isbn}&title={title}&author={author}&year={year}&pages={pages}") {
        fun createRoute(isbn: String = "", title: String = "", author: String = "", year: String = "", pages: String = ""): String {
            return "addBookScreen?isbn=$isbn&title=$title&author=$author&year=$year&pages=$pages"
        }
    }
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

    private val bookDatabaseViewModel by viewModels<BooksDatabaseView>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BooksDatabaseView() as T
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
                                    Navigation.StartScreen.route,
                                    bookDatabaseViewModel
                                )

                }
            }
        }
    }
}


@Composable
fun BottomNavigation(navController: NavHostController,accountViewModel: AccountViewModel,startDestination: String,booksDatabaseView: BooksDatabaseView){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Lista tras, na których pasek ma być ukryty
    val screensWithoutBottomBar = listOf(
        Navigation.StartScreen.route,
        Navigation.LoginScreen.route,
        Navigation.RegisterScreen.route,
        Navigation.LoginLoadingScreen.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute !in screensWithoutBottomBar) {
                NavigationBar() {
                    NavigationBarItem(
                        currentRoute == Navigation.HomeScreen.route,
                        { navController.navigate(Navigation.HomeScreen.route) },
                        icon = { Text("Home") })
                    NavigationBarItem(
                        currentRoute?.startsWith("addBookScreen") == true,
                        { navController.navigate(Navigation.AddBookScreen.createRoute()) },
                        icon = { Text("Add Book") })
                    NavigationBarItem(
                        selected = currentRoute == Navigation.ProfileScreen.route,
                        onClick = { navController.navigate(Navigation.ProfileScreen.route) },
                        icon = { Text("Profile") }
                    )
                }
            }
        }

    ) { innerPading->
        NavController(navController,accountViewModel, Modifier.padding(innerPading),startDestination,booksDatabaseView)

    }
}
@Composable
fun NavController(
    navController: NavHostController,
    accountViewModel: AccountViewModel,
    modifier: Modifier,
    startDestination: String,
    booksDatabaseView: BooksDatabaseView
) {
    NavHost(navController, startDestination = startDestination, modifier = modifier) {
    composable(Navigation.StartScreen.route) {
            if (Firebase.auth.currentUser == null) {
                StartScreen(navController)
            } else {
               LoginLoadingScreen(navController,booksDatabaseView)
            }
        }
        composable(Navigation.LoginScreen.route) {
            LoginScreen(navController, accountViewModel)
        }
        composable(Navigation.ProfileScreen.route) {
            if (Firebase.auth.currentUser == null) {
                navController.navigate(Navigation.LoginScreen.route)
            } else {
                ProfileScreen(navController, accountViewModel, booksDatabaseView)
            }
        }
        composable(Navigation.RegisterScreen.route) {
            RegisterScreen(navController, accountViewModel)
        }
        composable(Navigation.LoginLoadingScreen.route) {
            LoginLoadingScreen(navController,booksDatabaseView)
        }
        composable(Navigation.HomeScreen.route) {
            if (Firebase.auth.currentUser == null) {
            LoginScreen(navController,accountViewModel)
            } else {
            HomeScreen(booksDatabaseView,navController)
            }
            }
        composable(
            route = Navigation.AddBookScreen.route,
            arguments = listOf(
                navArgument("isbn") { defaultValue = "" },
                navArgument("title") { defaultValue = "" },
                navArgument("author") { defaultValue = "" },
                navArgument("year") { defaultValue = "" },
                navArgument("pages") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val author = backStackEntry.arguments?.getString("author") ?: ""
            val year = backStackEntry.arguments?.getString("year") ?: ""
            val pages = backStackEntry.arguments?.getString("pages") ?: ""

            if (Firebase.auth.currentUser == null) {
                LoginScreen(navController, accountViewModel)
            } else {
                AddBookScreen(
                    navController = navController,
                    booksDatabaseView = booksDatabaseView,
                    initialIsbn = isbn,
                    initialTitle = title,
                    initialAuthor = author,
                    initialYear = year,
                    initialPages = pages
                )
            }
        }
        composable(Navigation.AddWithIsbnScreen.route) {
            AddWithIsbnScreen(navController = navController)
        }
        composable(Navigation.BookDetailsScreen.route) { BookDetailsScreen(booksDatabaseView,navController) }

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
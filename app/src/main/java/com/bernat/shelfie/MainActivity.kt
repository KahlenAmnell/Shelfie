package com.bernat.shelfie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bernat.shelfie.data.local.AppDatabase
import com.bernat.shelfie.data.local.UserStats
import com.bernat.shelfie.ui.viewmodel.AccountViewModel
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView
import com.bernat.shelfie.ui.screens.auth.LoginLoadingScreen
import com.bernat.shelfie.ui.screens.auth.LoginScreen
import com.bernat.shelfie.ui.screens.auth.RegisterScreen
import com.bernat.shelfie.ui.screens.books.AddBookScreen
import com.bernat.shelfie.ui.screens.books.AddWithIsbnScreen
import com.bernat.shelfie.ui.screens.books.BookDetailsScreen
import com.bernat.shelfie.ui.screens.books.HomeScreen
import com.bernat.shelfie.ui.screens.ProfileScreen
import com.bernat.shelfie.ui.screens.books.EditBookScreen
import com.bernat.shelfie.ui.theme.ShelfieTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Date


sealed class Navigation(val route: String) {
    object StartScreen : Navigation("startScreen")
    object LoginScreen : Navigation("loginScreen")
    object RegisterScreen : Navigation("registerScreen")
    object HomeScreen : Navigation("homeScreen")
    object LoginLoadingScreen : Navigation("loginLoadingScreen")
    object AddWithIsbnScreen : Navigation("addWithIsbnScreen")

    object BookDetailsScreen : Navigation("bookDetailsScreen")
    object ProfileScreen : Navigation("profileScreen")
    object EditBookScreen : Navigation("editBookScreen")

    // Trasa z opcjonalnymi argumentami
    object AddBookScreen : Navigation("addBookScreen?isbn={isbn}&title={title}&author={author}&year={year}&pages={pages}&imageUrl={imageUrl}") {
        fun createRoute(isbn: String = "", title: String = "", author: String = "", year: String = "", pages: String = "", imageUrl: String = ""): String {
            val encodedUrl = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
            return "addBookScreen?isbn=$isbn&title=$title&author=$author&year=$year&pages=$pages&imageUrl=$encodedUrl"
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
        
        // Punkt 3: Room - Zapisywanie daty logowania
        val database = AppDatabase.getDatabase(this)
        if (Firebase.auth.currentUser != null) {
            lifecycleScope.launch {
                database.userStatsDao().insertOrUpdate(UserStats(lastLoginDate = Date().toString()))
            }
        }

        setContent {
            ShelfieTheme {
                Surface(Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    Firebase.database.setPersistenceEnabled(true)

                    MainScreenWrapper(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWrapper(
    navController: NavHostController,
    accountViewModel: AccountViewModel,
    startDestination: String,
    booksDatabaseView: BooksDatabaseView
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Ekrany bez nawigacji
    val authScreens = listOf(
        Navigation.StartScreen.route,
        Navigation.LoginScreen.route,
        Navigation.RegisterScreen.route,
        Navigation.LoginLoadingScreen.route
    )

    // Punkt 4: Navigation Drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute !in authScreens,
        drawerContent = {
            if (currentRoute !in authScreens) {
                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    Text("Shelfie Menu", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Mój Profil") },
                        selected = currentRoute == Navigation.ProfileScreen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Navigation.ProfileScreen.route)
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Wyloguj się") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Navigation.StartScreen.route) {
                                popUpTo(0) { inclusive = true }
                            }
                            booksDatabaseView.onLogOut()
                            accountViewModel.onLogout()
                        },
                        icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (currentRoute !in authScreens) {
                    TopAppBar(
                        title = { Text("Shelfie") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (currentRoute !in authScreens) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentRoute == Navigation.HomeScreen.route,
                            onClick = { navController.navigate(Navigation.HomeScreen.route) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = currentRoute?.startsWith("addBookScreen") == true,
                            onClick = { navController.navigate(Navigation.AddBookScreen.createRoute()) },
                            icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                            label = { Text("Add") }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavController(navController, accountViewModel, Modifier.padding(innerPadding), startDestination, booksDatabaseView)
        }
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
                LoginLoadingScreen(navController, booksDatabaseView, accountViewModel)
            }
        }
        composable(Navigation.LoginScreen.route) {
            LoginScreen(navController, accountViewModel)
        }
        composable(Navigation.ProfileScreen.route) {
            if (Firebase.auth.currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Navigation.LoginScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } else {
                ProfileScreen(navController, accountViewModel, booksDatabaseView)
            }
        }
        composable(Navigation.RegisterScreen.route) {
            RegisterScreen(navController, accountViewModel)
        }
        composable(Navigation.EditBookScreen.route) {
            EditBookScreen(navController,booksDatabaseView)
        }
        composable(Navigation.LoginLoadingScreen.route) {
            LoginLoadingScreen(navController, booksDatabaseView, accountViewModel)
        }
        composable(Navigation.HomeScreen.route) {
            if (Firebase.auth.currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Navigation.LoginScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } else {
                HomeScreen(booksDatabaseView, navController)
            }
        }
        composable(
            route = Navigation.AddBookScreen.route,
            arguments = listOf(
                navArgument("isbn") { defaultValue = "" },
                navArgument("title") { defaultValue = "" },
                navArgument("author") { defaultValue = "" },
                navArgument("year") { defaultValue = "" },
                navArgument("pages") { defaultValue = "" },
                navArgument("imageUrl") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val author = backStackEntry.arguments?.getString("author") ?: ""
            val year = backStackEntry.arguments?.getString("year") ?: ""
            val pages = backStackEntry.arguments?.getString("pages") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""

            if (Firebase.auth.currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Navigation.LoginScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } else {
                AddBookScreen(
                    navController = navController,
                    booksDatabaseView = booksDatabaseView,
                    initialIsbn = isbn,
                    initialTitle = title,
                    initialAuthor = author,
                    initialYear = year,
                    initialPages = pages,
                    initialImageUrl = imageUrl
                )
            }
        }
        composable(Navigation.AddWithIsbnScreen.route) {
            AddWithIsbnScreen(navController = navController)
        }
        composable(Navigation.BookDetailsScreen.route) { 
            BookDetailsScreen(booksDatabaseView, navController) 
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
                Image(imagePainter, null, Modifier.size(240.dp))
                Text(
                    text = "It's me, Shelfie!",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )
                Button(onClick = { navController.navigate(Navigation.LoginScreen.route) }) {
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

package com.bernat.shelfie.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bernat.shelfie.Navigation
import com.bernat.shelfie.R
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay

@Composable
fun LoginLoadingScreen(navController: NavController, booksDatabaseView: BooksDatabaseView) {
    
    // Stan animacji
    var startAnimation by remember { mutableStateOf(false) }
    
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "alphaAnimation"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scaleAnimation"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        // Skrócone opóźnienie do 500ms (tylko dla płynności animacji wejścia)
        delay(500) 

        if (Firebase.auth.currentUser != null) {
            booksDatabaseView.getRef()
            booksDatabaseView.loadData()
            navController.navigate(Navigation.HomeScreen.route) {
                popUpTo(Navigation.LoginLoadingScreen.route) { inclusive = true }
            }
        } else {
            navController.navigate(Navigation.LoginScreen.route) {
                popUpTo(Navigation.LoginLoadingScreen.route) { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale)
                    .alpha(alpha),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Shelfie Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Shelfie",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.alpha(alpha)
            )

            Text(
                text = "Twoja osobista biblioteka",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(64.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
        }
    }
}

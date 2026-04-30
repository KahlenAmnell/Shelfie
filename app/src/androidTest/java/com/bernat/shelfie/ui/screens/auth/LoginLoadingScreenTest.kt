package com.bernat.shelfie.ui.screens.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.bernat.shelfie.ui.viewmodel.BooksDatabaseView
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class LoginLoadingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginLoadingScreen_displaysAppTitleAndSubtitle() {
        // Given
        val mockNavController = mockk<NavController>(relaxed = true)
        val mockViewModel = mockk<BooksDatabaseView>(relaxed = true)

        // When
        composeTestRule.setContent {
            LoginLoadingScreen(
                navController = mockNavController,
                booksDatabaseView = mockViewModel
            )
        }

        // Then
        composeTestRule.onNodeWithText("Shelfie").assertIsDisplayed()
        composeTestRule.onNodeWithText("Twoja osobista biblioteka").assertIsDisplayed()
    }
}

package com.onopriienko.datingapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onopriienko.datingapp.ui.LikesScreen
import com.onopriienko.datingapp.ui.LoginScreen
import com.onopriienko.datingapp.ui.MutualLikesScreen
import com.onopriienko.datingapp.ui.RegistrationScreen
import com.onopriienko.datingapp.ui.SettingsScreen
import com.onopriienko.datingapp.ui.theme.DatingAppTheme

const val IS_LOGGED_IN = "is_logged_in"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DatingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferences = context.getSharedPreferences(
        "com.onopriienko.datingapp", Context.MODE_PRIVATE
    )
    val isLoggedIn = preferences.getBoolean(IS_LOGGED_IN, false)

    fun chooseStartDestination(): String {
        if (!isLoggedIn) {
            return "login"
        }
        val userId = preferences.getString("userId", null)
        return "likes/$userId"
    }

    NavHost(
        navController = navController,
        startDestination = chooseStartDestination()
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("registration") {
            RegistrationScreen(onRegistrationSuccess = { userId ->
                preferences.edit()
                    .putString("userId", userId)
                    .putBoolean(IS_LOGGED_IN, true)
                    .apply()
                navController.navigate("likes/$userId") {
                    popUpTo("registration") { inclusive = true }
                }
            })
        }
        composable("likes/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                LikesScreen(userId = userId, navController = navController)
            }
        }
        composable("mutual_likes") {
            MutualLikesScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

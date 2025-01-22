package com.onopriienko.datingapp.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.onopriienko.datingapp.IS_LOGGED_IN

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val preferences =
        context.getSharedPreferences("com.onopriienko.datingapp", Context.MODE_PRIVATE)
    val userId = preferences.getString("userId", null)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.weight(1f, fill = true),
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Налаштування")
                    }
                }

                Row(
                    modifier = Modifier.weight(1f, fill = true),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { navController.navigate("likes/$userId") }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Головна")
                    }
                }

                Row(
                    modifier = Modifier.weight(1f, fill = true),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate("mutual_likes") }
                    ) {
                        Text("Взаємні вподобайки", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Налаштування",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    preferences.edit()
                        .putBoolean(IS_LOGGED_IN, false)
                        .remove("userId")
                        .apply()
                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Вийти з аккаунту")
            }
        }
    }
}

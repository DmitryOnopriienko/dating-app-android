package com.onopriienko.datingapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.onopriienko.datingapp.ui.theme.DatingAppTheme

const val IS_LOGGED_IN = "is_logged_in"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DatingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        val preferences = getSharedPreferences(
                            "com.onopriienko.datingapp",
                            Context.MODE_PRIVATE
                        )
                        val isInitiallyLoggedIn = preferences.getBoolean(IS_LOGGED_IN, false)
                        var isLoggedIn by remember { mutableStateOf(isInitiallyLoggedIn) }

                        Button(
                            onClick = {
                                isLoggedIn = !isLoggedIn
                                preferences.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
                            },
                            modifier = Modifier.padding(innerPadding),
                        ) { Text("Change preferences") }
                        Greeting(
                            name = "Android".takeIf { isLoggedIn } ?: "(_)53|?/V4/\\/\\3",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DatingAppTheme {
        Greeting("Android")
    }
}

package com.onopriienko.datingapp.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.onopriienko.datingapp.IS_LOGGED_IN
import com.onopriienko.datingapp.model.LoginDto
import com.onopriienko.datingapp.service.UserApiClient
import com.onopriienko.datingapp.showToast
import com.onopriienko.datingapp.ui.component.PasswordInputField
import com.onopriienko.datingapp.ui.component.PhoneNumber
import kotlinx.coroutines.launch
import java.util.Base64

@Composable
fun LoginScreen(navController: NavController) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordHash by remember { mutableStateOf("") }

    val context = LocalContext.current
    val preferences =
        context.getSharedPreferences("com.onopriienko.datingapp", Context.MODE_PRIVATE)
    val coroutineScope = rememberCoroutineScope()

    fun canBeSubmitted(): Boolean = phoneNumber.length == 10 && passwordHash.isNotBlank()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            PhoneNumber(onValueChange = { phoneNumber = it })

            Spacer(modifier = Modifier.height(16.dp))

            PasswordInputField(
                modifier = Modifier.fillMaxWidth(),
                password = password,
                onPasswordChange = {
                    password = it
                    passwordHash = Base64.getEncoder().encodeToString(password.toByteArray())
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                enabled = canBeSubmitted(),
                onClick = {
                    coroutineScope.launch {
                        try {
                            val response =
                                UserApiClient.userService.login(LoginDto(phoneNumber, passwordHash))
                            if (response.isSuccessful) {
                                val userId = response.body()?.id ?: return@launch
                                preferences.edit()
                                    .putString("userId", userId)
                                    .putBoolean(IS_LOGGED_IN, true)
                                    .apply()
                                navController.navigate("likes/$userId") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                showToast(
                                    context,
                                    "Помилка: ${response.code()} ${response.message()}"
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            showToast(context, "Помилка: ${e.message}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Увійти")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navController.navigate("registration")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Немає акаунту? Зареєструватися")
            }
        }
    }
}

package com.onopriienko.datingapp.ui

import DatePicker
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.onopriienko.datingapp.IS_LOGGED_IN
import com.onopriienko.datingapp.enums.Sex
import com.onopriienko.datingapp.model.RegisterUserDto
import com.onopriienko.datingapp.service.UserApiClient
import com.onopriienko.datingapp.showToast
import com.onopriienko.datingapp.ui.component.AboutMeInput
import com.onopriienko.datingapp.ui.component.DropdownSelector
import com.onopriienko.datingapp.ui.component.ImageUploadComponent
import com.onopriienko.datingapp.ui.component.PasswordInputField
import com.onopriienko.datingapp.ui.component.PhoneNumber
import com.onopriienko.datingapp.ui.component.SexDropdownSelector
import com.onopriienko.datingapp.util.byteArrayToBase64
import com.onopriienko.datingapp.util.imageToByteArray
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Base64

@Composable
fun RegistrationScreen(onRegistrationSuccess: (String) -> Unit) {
    var cities by remember { mutableStateOf<List<String>>(emptyList()) }
    var city by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf<LocalDate?>(null) }
    var phoneNumber by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf<Sex?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordHash by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    var imageBase64 by remember { mutableStateOf("") }

    val context = LocalContext.current
    val preferences =
        context.getSharedPreferences("com.onopriienko.datingapp", Context.MODE_PRIVATE)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val response = UserApiClient.userService.getAvailableCities()
            if (response.isSuccessful) {
                cities = response.body() ?: emptyList()
            } else {
                showToast(context, "Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(context, "Error: ${e.message}")
        }
    }

    fun canBeSubmitted(): Boolean = city.isNotBlank() && name.isNotBlank() &&
            dateOfBirth != null && phoneNumber.length == 10 &&
            sex != null && passwordHash.isNotBlank() &&
            about.isNotBlank() && imageBase64.isNotBlank()

    fun buildRegistrationRequest(): RegisterUserDto = RegisterUserDto(
        name = name,
        dateOfBirth = dateOfBirth.toString(),
        city = city,
        phoneNumber = phoneNumber,
        sex = sex,
        passwordHash = passwordHash,
        about = about,
        encodedPicture = imageBase64,
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            DropdownSelector(
                availableOptions = cities,
                selectedOption = city,
                hintText = "Оберіть своє місто",
                onOptionSelected = { city = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
            )

            OutlinedTextField(
                value = name,
                label = { Text("Імʼя") },
                singleLine = true,
                onValueChange = { name = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )

            DatePicker(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                label = "Дата народження",
                onDateSelected = { dateOfBirth = it }
            )

            PhoneNumber(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onValueChange = { phoneNumber = it }
            )

            SexDropdownSelector(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                selectedSex = sex,
                onSexSelected = { sex = it }
            )

            PasswordInputField(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), password) {
                password = it
                passwordHash = Base64.getEncoder().encodeToString(password.toByteArray())
            }

            AboutMeInput(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onValueChange = { about = it })

            ImageUploadComponent(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onImageSelected = {
                    imageBase64 = byteArrayToBase64(imageToByteArray(context, it)!!)
                },
                onError = {
                    showToast(context, "Не вдалося завантажити зображення")
                }
            )

            Button(
                enabled = canBeSubmitted(),
                onClick = {
                    coroutineScope.launch {
                        val request = buildRegistrationRequest()
                        val response = UserApiClient.userService.registerUser(request)
                        if (response.isSuccessful) {
                            val userId = response.body()?.id ?: return@launch
                            onRegistrationSuccess(userId)
                        } else {
                            showToast(context, "Error: ${response.code()} ${response.message()}")
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text("Зареєструватися")
            }
        }
    }
}

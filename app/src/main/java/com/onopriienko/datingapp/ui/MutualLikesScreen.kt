package com.onopriienko.datingapp.ui

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.onopriienko.datingapp.model.UserDto
import com.onopriienko.datingapp.service.UserApiClient
import com.onopriienko.datingapp.showToast
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Base64

@Composable
fun MutualLikesScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var mutualLikes by remember { mutableStateOf<List<UserDto>>(emptyList()) }
    val userId =
        context.getSharedPreferences("com.onopriienko.datingapp", Context.MODE_PRIVATE)
            .getString("userId", "")

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = UserApiClient.userService.findMutual(userId ?: "")
                if (response.isSuccessful) {
                    mutualLikes = response.body() ?: emptyList()
                } else {
                    showToast(context, "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(context, "Error: ${e.message}")
            }
        }
    }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar {
                IconButton(onClick = { navController.navigate("settings") }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
                Spacer(modifier = Modifier.weight(1f, true))
                IconButton(onClick = { navController.navigate("likes/$userId") }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Likes")
                }
                Spacer(modifier = Modifier.weight(1f, true))
                TextButton(onClick = { navController.navigate("mutual_likes") }) {
                    Text("Likes")
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(mutualLikes.size) { index ->
                val user = mutualLikes[index]
                val age = user.dateOfBirth?.let {
                    Period.between(LocalDate.parse(it, dateFormatter), LocalDate.now()).years
                } ?: "N/A"

                val imageBitmap = user.pictureBase64?.let {
                    try {
                        val decodedBytes = Base64.getDecoder().decode(it)
                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        ) {
                            if (imageBitmap != null) {
                                Image(
                                    bitmap = imageBitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(
                                    "No Image",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${user.name.orEmpty()}, $age",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = user.phoneNumber.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

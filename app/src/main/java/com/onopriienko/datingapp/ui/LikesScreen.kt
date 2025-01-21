package com.onopriienko.datingapp.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.onopriienko.datingapp.enums.Reaction
import com.onopriienko.datingapp.model.ReactToUserDto
import com.onopriienko.datingapp.model.UserDto
import com.onopriienko.datingapp.service.UserApiClient
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.util.Base64

@Composable
fun LikesScreen(userId: String, navController: NavHostController) {
    var userToReactTo by remember { mutableStateOf<UserDto?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun loadNextUser() {
        coroutineScope.launch {
            val response = UserApiClient.userService.findNextToReactTo(userId)
            userToReactTo = if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    LaunchedEffect(Unit) {
        loadNextUser()
    }

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

        userToReactTo?.let { user ->
            val age = Period.between(LocalDate.parse(user.dateOfBirth), LocalDate.now()).years
            val imageBitmap = user.pictureBase64?.let {
                try {
                    val decodedBytes = Base64.getDecoder().decode(it)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                imageBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "User picture",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )
                } ?: Text(
                    text = "No picture available",
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = "${user.name}, $age, ${user.city}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = user.about ?: "",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(onClick = {
                        coroutineScope.launch {
                            UserApiClient.userService.react(
                                ReactToUserDto(
                                    fromUserId = userId,
                                    toUserId = user.id!!,
                                    reactionType = Reaction.LIKE,
                                )
                            )
                            loadNextUser()
                        }
                    }) {
                        Text("Like")
                    }
                    Button(onClick = {
                        coroutineScope.launch {
                            UserApiClient.userService.react(
                                ReactToUserDto(
                                    fromUserId = userId,
                                    toUserId = user.id!!,
                                    reactionType = Reaction.DISLIKE,
                                )
                            )
                            loadNextUser()
                        }
                    }) {
                        Text("Dislike")
                    }
                }
            }
        } ?: run {
            Text(
                text = "No more users to react to",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}


package com.onopriienko.datingapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutMeInput(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var aboutMe by remember { mutableStateOf("") }
    val charLimit = 300

    Column {
        OutlinedTextField(
            value = aboutMe,
            onValueChange = {
                if (it.length <= charLimit) {
                    aboutMe = it
                    onValueChange(it)
                }
            },
            label = { Text("Про мене") },
            placeholder = { Text("Розкажи про себе") },
            maxLines = 4,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = "${aboutMe.length} / $charLimit",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp)
        )
    }
}

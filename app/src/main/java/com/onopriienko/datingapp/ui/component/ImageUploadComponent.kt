package com.onopriienko.datingapp.ui.component

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun ImageUploadComponent(
    modifier: Modifier = Modifier,
    onImageSelected: (Uri) -> Unit,
    onError: (String) -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileSize = getFileSize(context, it)
            if (fileSize <= 1024 * 1024) { // 1MB
                imageUri = uri
                onImageSelected(uri)
                errorMessage = ""
            } else {
                errorMessage = "File size must be less than 2MB"
                onError(errorMessage)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Button
        Button(
            onClick = { launcher.launch("image/*") }
        ) {
            Text("Upload Profile Picture")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image
        imageUri?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(100.dp)
            )
        }

        errorMessage.takeIf { it.isNotEmpty() }?.let {
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun getFileSize(context: Context, uri: Uri): Long {
    val inputStream = context.contentResolver.openInputStream(uri)
    val returnable = inputStream?.available()?.toLong()
    inputStream?.close()
    return returnable ?: 0L
}

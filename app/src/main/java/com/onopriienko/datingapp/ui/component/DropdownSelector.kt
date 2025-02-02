package com.onopriienko.datingapp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DropdownSelector(
    availableOptions: List<String> = emptyList(),
    selectedOption: String? = null,
    hintText: String = "",
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(hintText) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Arrow"
                    )
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableOptions.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(text = option) },
                )
            }
        }
    }
}

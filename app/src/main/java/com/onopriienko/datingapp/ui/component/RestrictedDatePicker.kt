package com.onopriienko.datingapp.ui.component

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onopriienko.datingapp.ui.theme.DatingAppTheme
import java.time.LocalDate
import java.util.Calendar

@Composable // TODO remove this component
fun RestrictedDatePicker(onDateSelected: (LocalDate) -> Unit) {
    val calendar = Calendar.getInstance()

    calendar.add(Calendar.YEAR, -18)
    val maxDateInMillis = calendar.timeInMillis

    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDateDisplay by remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            selectedDateDisplay = "$dayOfMonth/${month + 1}/$year"
            onDateSelected(selectedDate)
        },
        currentYear, currentMonth, currentDay
    ).apply {
        datePicker.maxDate = maxDateInMillis
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Selected Date: $selectedDateDisplay",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { datePickerDialog.show() }) {
            Text(text = "Select Date")
        }
    }
}

@Composable
fun DatePickerDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RestrictedDatePicker { date ->
            println("Selected date: $date")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    DatingAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DatePickerDemo()
        }
    }
}

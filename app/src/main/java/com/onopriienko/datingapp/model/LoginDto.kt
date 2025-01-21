package com.onopriienko.datingapp.model

data class LoginDto(
    val phoneNumber: String,
    val passwordHash: String,
)

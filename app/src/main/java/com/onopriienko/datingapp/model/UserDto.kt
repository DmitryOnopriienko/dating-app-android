package com.onopriienko.datingapp.model

import com.onopriienko.datingapp.enums.Sex

data class UserDto(
    val id: String? = null,
    val name: String? = null,
    val dateOfBirth: String? = null,
    val city: String? = null,
    val phoneNumber: String? = null,
    val sex: Sex? = null,
    val passwordHash: String? = null,
    val about: String? = null,
    val pictureBase64: String? = null,
)

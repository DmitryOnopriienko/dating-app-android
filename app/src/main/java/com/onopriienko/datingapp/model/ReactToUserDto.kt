package com.onopriienko.datingapp.model

import com.onopriienko.datingapp.enums.Reaction

data class ReactToUserDto(
    val fromUserId: String,
    val toUserId: String,
    val reactionType: Reaction,
)

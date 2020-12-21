package com.example.ds3companion.model

data class User (
    val userId: String = "",
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val level: String? = "1",
    val location: String? = "Dungeon",
    val equipment: String? = null,
    val playtime: String? = "0"
)

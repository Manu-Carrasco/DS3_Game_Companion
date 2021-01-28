package com.example.ds3companion.model

data class User (
    val userId: String = "",
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val level: String? = "1",
    val location: String? = "Dungeon",
    val equipment: String? = null,
    val playtime: String? = "0",
    val head: String? = "0",
    val arms: String? = "0",
    val chest: String? = "0",
    val legs: String? = "0",
    val weapon1: String? = "0",
    val weapon2: String? = "0"
)

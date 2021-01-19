package com.example.ds3companion.model

import java.util.*

data class Chat (
        val message: String? = null,
        val username: String? = null,
        var time: Date = Date()
)
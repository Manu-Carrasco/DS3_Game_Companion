package com.example.ds3companion.model

data class Chat (
        val userId: String? = null,
        val message: String? = null,
        val sentAt: Long? = null,
        val isSent: Boolean? = null,
        val imageUrl: String? = null,

        val username: String? = null,
        val avatarUrl: String? = null

)
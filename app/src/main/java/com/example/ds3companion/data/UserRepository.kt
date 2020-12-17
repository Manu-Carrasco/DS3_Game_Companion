package com.example.ds3companion.data

import android.content.Context
import com.example.ds3companion.model.User

class UserRepository (
    private val firestoreDataSource: UserFirestoreDataSource = UserFirestoreDataSource(),
    private val localDataSource: UserLocalDataSource = UserLocalDataSource()
)
{
    fun getUserName(context: Context, userId: String, resultListener: ((String?) -> Unit)) {
        localDataSource.getUsername(context)?.let {username ->
            resultListener(username)
        } ?: run {
            firestoreDataSource.getUser(userId) { user: User? ->
                localDataSource.saveUsername(context, user?.username ?: "")
                resultListener(user?.username)
            }
        }
    }
}
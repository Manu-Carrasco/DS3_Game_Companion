package com.example.ds3companion.data

import com.example.ds3companion.Constants.COLLECTION_USERS
import com.example.ds3companion.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserFirestoreDataSource {
    fun getUser(userId: String, resultListener: ((User?) -> Unit) )
    {
        Firebase.firestore
            .collection(COLLECTION_USERS)
            .document(userId)
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val user = it.result?.toObject(User::class.java)
                    resultListener(user)
                } else {
                    resultListener(null)
                }
            }
    }
}
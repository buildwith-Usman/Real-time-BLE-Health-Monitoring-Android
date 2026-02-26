package com.app.dbraze.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val firebaseFireStore: FirebaseFirestore) :
    UserRepository {

    override suspend fun saveUserDataToFireStore(
        userId: String,
        userData: Map<String, Any>
    ): Task<Void> {
        try {
            return firebaseFireStore.collection("users").document(userId)
                .set(userData, SetOptions.merge())
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUserDataFromFireStore(userId: String): Task<Map<String, Any>?> {
        return firebaseFireStore.collection("users").document(userId).get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.data
            } else {
                null
            }
        }
    }
}

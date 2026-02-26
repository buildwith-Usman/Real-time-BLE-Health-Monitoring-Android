package com.app.dbraze.data.repository

import com.google.android.gms.tasks.Task

interface UserRepository {
    suspend fun saveUserDataToFireStore(userId: String, userData: Map<String, Any>): Task<Void>
    suspend fun getUserDataFromFireStore(userId: String): Task<Map<String, Any>?>
}

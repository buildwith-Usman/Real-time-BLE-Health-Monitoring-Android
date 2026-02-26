package com.app.dbraze.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun createUser(email: String, password: String): Task<AuthResult>
    suspend fun loginUser(email: String, password: String): Task<AuthResult>
    suspend fun resetPassword(email: String): Task<Void>
    fun getCurrentUser(): FirebaseUser?
    fun logout()
}
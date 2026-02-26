package com.app.dbraze.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun createUser(email: String, password: String): Task<AuthResult> {
        try {
            return firebaseAuth.createUserWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun loginUser(email: String, password: String): Task<AuthResult> {
        try {
            return firebaseAuth.signInWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun resetPassword(email: String): Task<Void> {
        try {
            return firebaseAuth.sendPasswordResetEmail(email)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}


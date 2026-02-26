package com.app.dbraze.di

import com.app.dbraze.data.repository.AuthRepository
import com.app.dbraze.data.repository.AuthRepositoryImpl
import com.app.dbraze.data.repository.UserRepository
import com.app.dbraze.data.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseFireStore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(firebaseFireStore)
    }
}

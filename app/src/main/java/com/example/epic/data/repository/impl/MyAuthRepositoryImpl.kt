package com.example.epic.data.repository.impl

import com.example.epic.data.repository.MyAuthRepository
import com.example.epic.data.repository.await
import com.example.epic.util.ResultWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class MyAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : MyAuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): ResultWrapper<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            ResultWrapper.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ResultWrapper.Failure(e)
        }
    }

    override suspend fun signup(name: String, email: String, password: String): ResultWrapper<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            ResultWrapper.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ResultWrapper.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
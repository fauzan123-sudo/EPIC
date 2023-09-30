package com.example.epic.data.repository

import com.example.epic.util.ResultWrapper
import com.google.firebase.auth.FirebaseUser

//class AuthRepository @Inject constructor(val firebase:): BaseFirebaseRepository() {
//}

interface MyAuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): ResultWrapper<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): ResultWrapper<FirebaseUser>
    fun logout()


}
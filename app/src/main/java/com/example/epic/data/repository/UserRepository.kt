package com.example.epic.data.repository

import com.example.epic.data.model.user.User
import com.example.epic.data.model.user.UserRequest
import com.example.epic.util.Resource
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UserRepository @Inject constructor() : BaseFirebaseRepository() {

    private val database = FirebaseDatabase.getInstance().reference.child("users")

    suspend fun registerUser(request: UserRequest): Resource<Unit> {
        return safeFirebaseCall {
            val userId = database.push().key
            userId?.let {
                val newUser = UserRequest(userId, request.name, request.username, request.password)
                database.child(it).setValue(newUser)
                Resource.Success(Unit)
            } ?: Resource.Error("Gagal menambahkan pengguna ke database", null)
        }
    }

//    suspend fun registerUser(name: String, username: String, password: String): Resource<Unit> {
//        return safeFirebaseCall {
//            val userId = database.push().key
//            userId?.let {
//                val newUser = User(userId, name, username, password)
//                database.child(it).setValue(newUser)
//            }
//        }
//    }

    suspend fun loginUser(username: String, password: String): Resource<User?> {
        return safeFirebaseCall {
            val query = database.orderByChild("username").equalTo(username)
            val result = query.get().await()

            if (result.exists()) {
                val user = result.children.first().getValue(User::class.java)
                if (user?.password == password) {
                    user
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

//    suspend fun loginUser(username: String, password: String): Resource<Unit> {
//        return safeFirebaseCall {
//            val query = database.orderByChild("username").equalTo(username)
//            val result = query.get().await()
//
//        }
//    }


}
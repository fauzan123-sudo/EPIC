package com.example.epic.data.repository

import com.example.epic.data.model.Notification
import com.example.epic.data.model.NotificationData
//import com.example.epic.data.model.NotificationData
import com.example.epic.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NotificationRepository @Inject constructor() : BaseFirebaseRepository() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("user")

    suspend fun addNotification(notification: Notification): Resource<Unit> {
        return safeFirebaseCall {
            val notificationRef = database.child(notification.title ?: "")
            notificationRef.setValue(notification)
        }
    }

    fun getNotificationFlow(id: String): Flow<Resource<Notification>> = flow {
        emit(Resource.Loading())
        val notificationRef = database.child(id)
        val notificationSnapshot = notificationRef.get().await()

        val notification = notificationSnapshot.getValue(Notification::class.java)
        if (notification != null) {
            emit(Resource.Success(notification))
        } else {
            emit(Resource.Error("Notification not found"))
        }
    }.flowOn(Dispatchers.IO)

    fun getAllNotificationsRealtime(): Flow<Resource<List<Notification>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notificationList = mutableListOf<Notification>()
                for (childSnapshot in snapshot.children) {
                    val notification = childSnapshot.getValue(Notification::class.java)
                    notification?.let { notificationList.add(it) }
                }
                trySend(Resource.Success(notificationList))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(message = error.message))
            }
        }

        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) }
    }

    fun getAllNotifications(): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading())

        val notificationList = mutableListOf<Notification>()
        val snapshot = database.get().await()

        for (childSnapshot in snapshot.children) {
            val notification = childSnapshot.getValue(Notification::class.java)
            notification?.let { notificationList.add(it) }
        }

        emit(Resource.Success(notificationList))
    }

    suspend fun updateNotificationById(id: String, updatedNotification: Notification): Resource<Unit> {
        return safeFirebaseCall {
            val notificationRef = database.child(id)
            notificationRef.setValue(updatedNotification)
        }
    }

    suspend fun markNotificationAsRead(notificationId: String): Resource<Unit> {
        return safeFirebaseCall {
            val notificationRef = database.child(notificationId)
            val updateMap = mapOf("read" to true)
            notificationRef.updateChildren(updateMap)
        }
    }


    suspend fun deleteNotification(notificationId: String): Resource<Unit> {
        return safeFirebaseCall {
            database.child(notificationId).removeValue()
        }
    }

    fun getNotificationsByNip(nip: String): Flow<Resource<List<NotificationData>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notificationList = mutableListOf<NotificationData>()
                for (childSnapshot in snapshot.children) {
                    val notification = childSnapshot.getValue(NotificationData::class.java)
                    if (notification?.title == nip) {
                        notification?.let { notificationList.add(it) }
                    }
                }
                trySend(Resource.Success(notificationList))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(message = error.message))
            }
        }

        database.addValueEventListener(listener)

        awaitClose { database.removeEventListener(listener) }
    }


    fun getNotificationsByNipFlow(nip: String): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading())
        try {
            val query = database.orderByChild("nip").equalTo(nip)
            val dataSnapshot = query.get().await()
            val notifications = mutableListOf<Notification>()

            for (snapshot in dataSnapshot.children) {
                val notification = snapshot.getValue(Notification::class.java)
                notification?.let {
                    notifications.add(it)
                }
            }

            emit(Resource.Success(notifications))
        } catch (e: Exception) {
            emit(Resource.Error(e.toString()))
        }
    }

}
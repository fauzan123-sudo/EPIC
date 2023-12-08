package com.example.epic.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.epic.R
import com.example.epic.data.model.NotificationData
import com.example.epic.data.repository.DatabaseRepository
import com.example.epic.data.room.ContactsDB
import com.example.epic.data.room.ContactsDao
import com.example.epic.data.room.ContactsEntity
import com.example.epic.ui.activity.MainActivity
import com.example.epic.util.Constants.CONTACTS_DATABASE
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
//    private lateinit var adapter: NotificationAdapter
    private lateinit var repository: DatabaseRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseToken", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
//        adapter = NotificationAdapter()
        val data = remoteMessage.data

        val title = data["title"]
        val body = data["body"]

        showNotification(title,body)
        saveNotification(title.toString(),body.toString())

    }

    private fun saveNotification(title: String, body: String) {
        val contactsEntity = ContactsEntity(
            title = title,
            body = body,
            isRead = false,
            timestamp = System.currentTimeMillis()
        )

        val databaseRepository = DatabaseRepository(provideDao(applicationContext))
        CoroutineScope(Dispatchers.IO).launch {
            databaseRepository.saveContact(contactsEntity)
        }

        val notificationData = NotificationData(title, body)
        val notifications = Paper.book().read(Constants.notify, mutableListOf<NotificationData>())
        Log.d("data", "$notifications")
        notifications!!.add(notificationData)
        Paper.book().write(Constants.notify, notifications)
//        adapter.setNotifications(notifications)
//        adapter.notifyDataSetChanged()
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "channel_id"
        val channelName = "channel_name"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(123, notificationBuilder.build())
    }

    private fun provideDao(context: Context): ContactsDao {
        val database = Room.databaseBuilder(
            context, ContactsDB::class.java, CONTACTS_DATABASE
        ).build()
        return database.contactsDao()
    }



}

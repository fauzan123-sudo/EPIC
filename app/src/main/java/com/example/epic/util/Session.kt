package com.example.epic.util

import com.example.epic.data.model.NotificationData
import com.example.epic.util.Constans.notifyCount
import io.paperdb.Paper

    fun saveNotification(data:NotificationData) {
        Paper.book().write(notifyCount, data)
    }

    fun saveNotificationCount(data:NotificationData) {
        Paper.book().write(notifyCount, data)
    }

//    fun getData(): LoginResponse? {
//        return Paper.book().read<LoginResponse>("user")
//    }

    fun deleteData() {
        Paper.book().delete("user")
    }

    fun getNotification(): NotificationData? {
        return Paper.book().read(notifyCount)
    }

    fun getNotificationCount(): Int? {
        return Paper.book().read(notifyCount, 0)
    }

    fun deleteNotificationCount() {
        Paper.book().delete(notifyCount)
    }

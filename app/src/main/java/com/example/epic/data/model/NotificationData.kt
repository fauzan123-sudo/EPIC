package com.example.epic.data.model

data class NotificationData(
    val title: String?,
    val body: String?,
    var isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)


//data class NotificationItem(
//    val title: String,
//    val description: String,
//    val time: String,
//    var isNew: Boolean = true,
//    var color: Int = Color.BLUE
//)

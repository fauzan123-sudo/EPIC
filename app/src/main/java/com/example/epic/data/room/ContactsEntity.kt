package com.example.epic.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.epic.util.Constans.CONTACTS_TABLE

@Entity(tableName = CONTACTS_TABLE)
data class ContactsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title:String? = "",
    var body:String? = "",
    var isRead: Boolean = false,
    var timestamp: Long = System.currentTimeMillis()
)
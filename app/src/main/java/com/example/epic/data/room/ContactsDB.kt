package com.example.epic.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.epic.data.model.user.management.read.Data

@Database(entities = [ContactsEntity::class, Data::class], version = 2, exportSchema = false)
abstract class ContactsDB : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
    abstract fun storeNameDao(): StoreDao
}
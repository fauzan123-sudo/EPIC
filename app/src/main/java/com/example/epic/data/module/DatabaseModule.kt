package com.example.epic.data.module

import android.content.Context
import androidx.room.Room
import com.example.epic.data.model.user.management.read.Data
import com.example.epic.data.room.ContactsDB
import com.example.epic.data.room.ContactsEntity
import com.example.epic.util.Constants.CONTACTS_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, ContactsDB::class.java, CONTACTS_DATABASE
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideDao(db: ContactsDB) = db.contactsDao()

    @Provides
    @Singleton
    fun storeNameDao(db: ContactsDB) = db.storeNameDao()


    @Provides
    fun provideEntity() = ContactsEntity()

    @Provides
    fun provideNewContactsEntity() = Data()

}
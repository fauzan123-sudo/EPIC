package com.example.epic.data.room

import androidx.room.*
import com.example.epic.util.Constans.CONTACTS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveContact( contactsEntity: ContactsEntity)

    @Update
    fun updateContact(contactsEntity: ContactsEntity)

    @Delete
    fun deleteContact(contactsEntity: ContactsEntity)

    @Query("SELECT * FROM $CONTACTS_TABLE WHERE id ==:id")
    fun getContact(id: Int): Flow<ContactsEntity>

    @Query("SELECT * FROM $CONTACTS_TABLE")
    fun getAllContacts(): Flow<List<ContactsEntity>>

    @Query("DELETE FROM $CONTACTS_TABLE")
    fun deleteAllContacts()

}
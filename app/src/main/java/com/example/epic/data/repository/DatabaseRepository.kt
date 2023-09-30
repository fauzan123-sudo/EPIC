package com.example.epic.data.repository

import com.example.epic.data.room.ContactsDao
import com.example.epic.data.room.ContactsEntity
import javax.inject.Inject
class DatabaseRepository @Inject constructor(private val dao: ContactsDao) {
    fun saveContact(entity : ContactsEntity)=dao.saveContact(entity)
    fun updateTask(entity: ContactsEntity)= dao.updateContact(entity)
    fun deleteContact(entity : ContactsEntity)=dao.deleteContact(entity)
    fun getDetailsContact(id:Int)= dao.getContact(id)
    fun getAllContacts()=dao.getAllContacts()
    fun deleteAllContacts()=dao.deleteAllContacts()


}
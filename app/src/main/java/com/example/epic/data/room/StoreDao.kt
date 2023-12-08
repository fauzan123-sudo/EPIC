package com.example.epic.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.epic.data.model.user.management.read.Data
import com.example.epic.util.Constants.STORE_NAME


@Dao
interface StoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(data: Data)

    @Delete
    fun delete(item: Data)

    @Query("SELECT * FROM $STORE_NAME LIMIT 1")
    fun getSingleStore(): LiveData<Data>
}
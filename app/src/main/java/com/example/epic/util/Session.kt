package com.example.epic.util

import com.example.epic.data.model.NotificationData
import com.example.epic.data.model.user.login.LoginResponse
import com.example.epic.data.model.user.management.read.Data
import com.example.epic.data.model.user.management.read.UserListResponse
import com.example.epic.util.Constants.STORE_NAME
import com.example.epic.util.Constants.USER_ID
import com.example.epic.util.Constants.USER_LIST
import com.example.epic.util.Constants.USER_SAVED
import com.example.epic.util.Constants.notifyCount
import io.paperdb.Paper

fun saveNotification(data: NotificationData) {
    Paper.book().write(notifyCount, data)
}

fun saveNotificationCount(data: NotificationData) {
    Paper.book().write(notifyCount, data)
}

//    fun getData(): LoginResponse? {
//        return Paper.book().read<LoginResponse>("user")
//    }


fun getNotification(): NotificationData? {
    return Paper.book().read(notifyCount)
}

fun getNotificationCount(): Int? {
    return Paper.book().read(notifyCount, 0)
}

fun deleteNotificationCount() {
    Paper.book().delete(notifyCount)
}

fun saveUser(data: LoginResponse) {
    Paper.book().write(USER_SAVED, data)
}

fun readLoginResponse(): LoginResponse? {
    return Paper.book().read<LoginResponse>(USER_SAVED, null)
}


fun deleteUserData() {
    Paper.book().delete(USER_SAVED)
}

fun saveStoreName(value: String) {
    Paper.book().write(STORE_NAME, value)
}

fun saveUserId(value: String) {
    Paper.book().write(USER_ID, value)
}

fun getUserId(): String? {
    val userId: String? = Paper.book().read<String>(USER_ID, null)
    if (userId != null) {
        println("ID Pengguna yang Dibaca: $userId")
    } else {
        println("ID Pengguna tidak ditemukan.")
    }
    return userId
}

fun getStoreName(): String? {
    val storeName: String? = Paper.book().read<String>(STORE_NAME, null)
    if (storeName != null) {
        println("Nama Pengguna yang Dibaca: $storeName")
    } else {
        println("Nama Pengguna tidak ditemukan.")
    }
    return storeName
}

fun deleteUserId(): Boolean {
    val isDataExist: Boolean = Paper.book().contains(USER_ID)

    return if (isDataExist) {
        Paper.book().delete(USER_ID)
        println("Data ID Pengguna berhasil dihapus.")
        true
    } else {
        println("Gagal menghapus data ID Pengguna atau data ID Pengguna tidak ditemukan.")
        false
    }
}

fun deleteStoreName(): Boolean {
    val isDataExist: Boolean = Paper.book().contains(STORE_NAME)

    return if (isDataExist) {
        Paper.book().delete(STORE_NAME)
        println("Data Nama Pengguna berhasil dihapus.")
        true
    } else {
        println("Gagal menghapus data Nama Pengguna atau data Nama Pengguna tidak ditemukan.")
        false
    }
}

fun updateUserId(newValue: String) {
    val existingUserId: String? = getUserId()
    if (existingUserId != null) {
        Paper.book().delete(USER_ID)
        Paper.book().write(USER_ID, newValue)
        println("ID Pengguna berhasil diperbarui dari $existingUserId menjadi $newValue")
    } else {
        println("Tidak dapat memperbarui ID Pengguna karena ID Pengguna sebelumnya tidak ditemukan.")
    }
}

fun updateStoreName(newValue: String) {
    val existingStoreName: String? = getStoreName()
    if (existingStoreName != null) {
        Paper.book().delete(STORE_NAME)
        Paper.book().write(STORE_NAME, newValue)
        println("Nama Pengguna berhasil diperbarui dari $existingStoreName menjadi $newValue")
    } else {
        println("Tidak dapat memperbarui Nama Pengguna karena Nama Pengguna sebelumnya tidak ditemukan.")
    }
}

fun readStore(): UserListResponse? {
    return Paper.book().read<UserListResponse>(USER_LIST, null)
}

fun saveStore(newData: Data) {
    val currentData = readStore() ?: UserListResponse(emptyList(), "", false)

    val newDataList = currentData.data.toMutableList()
    newDataList.add(newData)

    saveStore(currentData.copy(data = newDataList))
}

fun saveStore(updatedData: UserListResponse) {
    Paper.book().write(USER_LIST, updatedData)
}

fun deleteStore() {
    Paper.book().delete(USER_LIST)
}

fun updateStore(updatedData: Data) {
    val currentData = readStore() ?: UserListResponse(emptyList(), "", false)

    val dataIndex = currentData.data.indexOfFirst { it.id_user == updatedData.id_user }

    if (dataIndex != -1) {
        val newDataList = currentData.data.toMutableList()
        newDataList[dataIndex] = updatedData

        saveStore(currentData.copy(data = newDataList))
    }
}







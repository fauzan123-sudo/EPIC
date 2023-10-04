package com.example.epic.data.repository

import com.example.epic.data.network.UserManagementApi
import javax.inject.Inject

class UserManagementRepository @Inject constructor(private val api: UserManagementApi) :
    BaseRepository() {

}
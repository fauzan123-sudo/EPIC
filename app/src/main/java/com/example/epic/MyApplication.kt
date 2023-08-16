package com.example.epic

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
        FirebaseApp.initializeApp(this)

    }
}
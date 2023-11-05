package com.example.epic.ui.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    val selectedImage: MutableLiveData<Uri?> = MutableLiveData()

    val buttonClickCamera = MutableLiveData<Boolean>()
    val buttonClickGallery = MutableLiveData<Boolean>()

    fun buttonClickCamera() {
        buttonClickCamera.value = true
    }

    fun buttonClickGallery() {
        buttonClickCamera.value = true
    }
}
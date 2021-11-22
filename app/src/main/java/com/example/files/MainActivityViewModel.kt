package com.example.files

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val repository = MainActivityRepository()
    var url = mutableStateOf("")
    var fileName = mutableStateOf("")
    var active = mutableStateOf(true)
    var isLoading = mutableStateOf(false)


    private val _downloadStatus = MutableLiveData<Boolean>()
    val downloadStatus: LiveData<Boolean>
        get() = _downloadStatus

    fun updateUrl(newUrl: String) {
        url.value = newUrl
    }

    fun setDownloadToFalse() {
        _downloadStatus.value = false
    }

    fun getFileFromUrl(url: String, context: Context) {
        viewModelScope.launch {
            isLoading.value = true
            active.value = false
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@launch
            fileName.value = repository.getFileFromUrl(url, context)
            active.value = true
            isLoading.value = false
            _downloadStatus.postValue(true)
        }
    }

    fun firstLaunchDownload(context: Context) {
        viewModelScope.launch {
            repository.firstLaunchDownload(context)
        }
    }

}
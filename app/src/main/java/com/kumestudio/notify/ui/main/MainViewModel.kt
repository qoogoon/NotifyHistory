package com.kumestudio.notify.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kumestudio.notify.db.NotificationData

class MainViewModel : ViewModel() {
    val textValue: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val listData: MutableLiveData<MutableList<NotificationData>> =
        MutableLiveData()

    // TODO: Implement the ViewModel
}
package com.hamletleon.randomusers.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val twoPane = MutableLiveData<Boolean>()
    val favorites = MutableLiveData<String>()
}

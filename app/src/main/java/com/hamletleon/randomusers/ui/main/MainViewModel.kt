package com.hamletleon.randomusers.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hamletleon.randomusers.dtos.UserDto
import com.hamletleon.randomusers.models.User
import com.hamletleon.randomusers.ui.main.adapters.UsersAdapter

class MainViewModel : ViewModel() {
    val twoPane = MutableLiveData<Boolean>()
    var usersAdapter: UsersAdapter<MainFragment>? = null
    var favoritesAdapter: UsersAdapter<MainFragment>? = null

    val users = mutableListOf<User>()
    val favorites = mutableListOf<User>()
}

package com.hamletleon.randomusers.ui.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hamletleon.randomusers.models.User
import com.hamletleon.randomusers.repositories.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserDetailsViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val repository = UsersRepository(application)
    val user = MutableLiveData<User>()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    fun getUserDetails(userId: Int) {
        launch {
            user.value = repository.getUserById(userId)
        }
    }
}
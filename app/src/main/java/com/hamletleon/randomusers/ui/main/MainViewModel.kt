package com.hamletleon.randomusers.ui.main

import android.app.Application
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.randomusers.dtos.LoadingProgress
import com.hamletleon.randomusers.models.User
import com.hamletleon.randomusers.repositories.UsersRepository
import com.hamletleon.randomusers.ui.users.adapters.UsersAdapter
import com.hamletleon.randomusers.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import com.hamletleon.randomusers.R

class MainViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    val loading = MutableLiveData<LoadingProgress>()
    private val repository: UsersRepository = UsersRepository(application)
    var twoPane: Boolean = false
    var lastOrientation: Int = 0

    val notifyMainList = MutableLiveData<Boolean>()
    val notifyFavoriteList = MutableLiveData<Boolean>()

    var usersAdapter: UsersAdapter? = null
    var usersSuggestionsAdapter: SimpleCursorAdapter? = null

    var favoritesAdapter: UsersAdapter? = null

    var lastUsers = listOf<User>()

    var lastFavorites = listOf<User>()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    private var page = 1
    fun scrollListener(manager: GridLayoutManager) = object: EndlessRecyclerViewScrollListener(manager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
            this@MainViewModel.page = page
            if (usersAdapter?.filtered == false) getUsers(page)
        }
    }

    fun getUsers(page: Int = 1) {
        if (usersAdapter == null) loading.value = LoadingProgress(R.string.loading_users)
        launch {
            repository.getUsers(page).await().let { it ->
                lastUsers = it.toMutableList()
                notifyMainList.value = true
                if (loading.value != null) loading.value = null
            }
        }
    }

    fun getFavorites() {
        launch {
            repository.getFavoriteUsers().await().let {
                lastFavorites = it
                notifyFavoriteList.value = true
            }
        }
    }
}

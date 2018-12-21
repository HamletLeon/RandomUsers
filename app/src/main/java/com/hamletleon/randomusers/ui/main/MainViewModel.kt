package com.hamletleon.randomusers.ui.main

import android.app.Application
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.randomusers.models.User
import com.hamletleon.randomusers.repositories.UsersRepository
import com.hamletleon.randomusers.ui.users.adapters.UsersAdapter
import com.hamletleon.randomusers.utils.EndlessRecyclerViewScrollListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val repository: UsersRepository = UsersRepository(application)
    var twoPane: Boolean = false

    val notifyMainList = MutableLiveData<Boolean>()
    val notifyFavoriteList = MutableLiveData<Boolean>()

    var usersAdapter: UsersAdapter? = null
    var usersSuggestionsAdapter: SimpleCursorAdapter? = null

    var favoritesAdapter: UsersAdapter? = null

    var lastUsers = listOf<User>()
    var lastSuggestions = listOf<String>() // Simple lastSuggestions implementation

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
        launch {
            repository.getUsers(page).await().let { it ->
                lastUsers = it.toMutableList()
                lastSuggestions = it.map { it.fullName }
                notifyMainList.value = true
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

package com.hamletleon.randomusers.ui.main

import android.app.Application
import android.widget.SimpleCursorAdapter
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

    var usersAdapter: UsersAdapter<MainFragment>? = null
    var usersSuggestionsAdapter: SimpleCursorAdapter? = null

    var favoritesAdapter: UsersAdapter<MainFragment>? = null

    val twoPane = MutableLiveData<Boolean>()

    val users = MutableLiveData<List<User>>()
    val suggestions = MutableLiveData<List<String>>() // Simple suggestions implementation

    val favorites = mutableListOf<User>()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    private var page = 1
    fun scrollListener(manager: GridLayoutManager) = object: EndlessRecyclerViewScrollListener(manager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
            this@MainViewModel.page = page
            getUsers(page)
        }
    }

    fun getUsers(page: Int = 1) {
        launch {
            val response = repository.getUsers(page).await()
            users.value = response.toMutableList()
            suggestions.value = response.map { it.fullName }
        }
    }
}

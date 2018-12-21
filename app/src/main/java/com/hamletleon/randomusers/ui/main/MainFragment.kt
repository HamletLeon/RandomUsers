package com.hamletleon.randomusers.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleCursorAdapter
import androidx.cursoradapter.widget.CursorAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.randomusers.R
import com.hamletleon.randomusers.databinding.MainFragmentBinding
import com.hamletleon.randomusers.models.User
import com.hamletleon.randomusers.ui.users.adapters.UsersAdapter
import com.hamletleon.randomusers.utils.calculateScreenSizeAndItemsOnIt
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {
    private var viewModel: MainViewModel? = null
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.model = viewModel
        binding.setLifecycleOwner(this)

        setListeners()

        if (viewModel?.usersAdapter != null) resetMainList()
        viewModel?.getUsers()

        if (viewModel?.favoritesAdapter != null) resetFavoriteList()
        viewModel?.getFavorites()
    }

    private fun setListeners() {
        viewModel?.notifyMainList?.observe(this, Observer {
            if (it == true) {
                val users = viewModel?.lastUsers
                if (allList.adapter == null && users != null && users.isNotEmpty()) setAdapter(allList, users)
                else if (users != null && users.isNotEmpty()) viewModel?.usersAdapter?.addUsers(users)
            }
        })

        viewModel?.notifyFavoriteList?.observe(this, Observer {
            if (it == true) {
                val users = viewModel?.lastFavorites
                if (favoritesList.adapter == null && users != null && users.isNotEmpty()) setAdapter(favoritesList, users, false)
                else if (users != null && users.isNotEmpty()) viewModel?.favoritesAdapter?.addUsers(users)
            }
        })

//        viewModel.lastUsers.observe(this, Observer {
//            if (it != null && it.isNotEmpty() && binding.allList?.adapter == null) setAdapter(it, viewModel.usersAdapter.value)
//            else if (it != null && it.isNotEmpty()) viewModel.usersAdapter.value?.addUsers(it)
//        })
//
//        viewModel.lastSuggestions.observe(this, Observer {
//            if (viewModel.usersSuggestionsAdapter == null) setSuggestionsAdapter(it)
//            else {
////                viewModel.usersSuggestionsAdapter
//            }
//        })
//
//        viewModel.lastFavorites.observe(this, Observer {
//            if (binding.favoritesList?.adapter == null && it != null && it.isNotEmpty()) setAdapter(it, viewModel.favoritesAdapter.value, false)
//            else if (it != null && it.isNotEmpty()) viewModel.favoritesAdapter.value?.addUsers(it)
//        })
    }

//    private fun setAdapter(lastUsers: List<User>? = null, createdAdapter: UsersAdapter<MainFragment>? = null, mainAdapter: Boolean = true) {
//        if (mainAdapter)
//        {
//            val (manager, adapter) = setAdapter(binding.allList, lastUsers, createdAdapter, mainAdapter)
//            binding.allList?.layoutManager = manager
//            val scrollListener = viewModel.scrollListener(manager)
//            binding.allList?.addOnScrollListener(scrollListener)
//            viewModel.usersAdapter.value = adapter
//        } else {
//            val (manager, adapter) = setAdapter(binding.favoritesList, lastUsers, createdAdapter, mainAdapter)
//            binding.favoritesList?.layoutManager = manager
//            viewModel.favoritesAdapter.value = adapter
//            binding.favoritesLayout?.visibility = View.VISIBLE
//            binding.allTitle?.visibility = View.VISIBLE
//        }
//    }

    private fun setAdapter(recyclerView: RecyclerView, users: List<User>?, mainAdapter: Boolean = true) {
        val manager = initManager(recyclerView, mainAdapter)
        val adapter = UsersAdapter(this, users)
        if (mainAdapter) {
            viewModel?.usersAdapter = adapter
            viewModel?.scrollListener(manager)?.let {
                recyclerView.addOnScrollListener(it)
            }
            recyclerView.adapter = viewModel?.usersAdapter
        }
        else {
            viewModel?.favoritesAdapter = adapter
            if (users != null && users.isNotEmpty()) {
                favoritesLayout.visibility = View.VISIBLE
                allTitle.visibility = View.VISIBLE
            } else {
                favoritesLayout.visibility = View.GONE
                allTitle.visibility = View.GONE
            }
            recyclerView.adapter = viewModel?.favoritesAdapter
        }
    }
    private fun initManager(recyclerView: RecyclerView, mainAdapter: Boolean = true, itemSizeDpHeight: Int = 80, itemSizeDpWidth: Int = 80): GridLayoutManager {
        val (_, itemsOnScreen) = activity.calculateScreenSizeAndItemsOnIt(itemSizeDpHeight, itemSizeDpWidth)
        val manager = GridLayoutManager(context, if (mainAdapter) itemsOnScreen.itemsOnWidth else 1,
            if (mainAdapter) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL, false)
        recyclerView.layoutManager = manager
        return manager
    }

    private fun setSuggestionsAdapter(suggestions: List<String>)
    {
        val suggestionsAdapter = SimpleCursorAdapter(activity, android.R.layout.simple_list_item_1, null, suggestions.toTypedArray(),
            intArrayOf(android.R.id.text1), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        viewModel?.usersSuggestionsAdapter = suggestionsAdapter
    }

    private fun resetMainList() {
        viewModel?.lastUsers = listOf()
        viewModel?.usersAdapter = null
    }

    private fun resetFavoriteList() {
        viewModel?.lastFavorites = listOf()
        viewModel?.favoritesAdapter = null
    }

    override fun onPause() {
        super.onPause()
        viewModel?.notifyMainList?.removeObservers(this)
        viewModel?.notifyFavoriteList?.removeObservers(this)
    }
}

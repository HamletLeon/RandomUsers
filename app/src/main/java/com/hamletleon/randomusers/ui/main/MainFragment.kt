package com.hamletleon.randomusers.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
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
    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
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
                setSuggestionsAdapter()
            }
        })

        viewModel?.notifyFavoriteList?.observe(this, Observer {
            if (it == true) {
                val users = viewModel?.lastFavorites
                if (favoritesList.adapter == null && users != null && users.isNotEmpty()) setAdapter(
                    favoritesList,
                    users,
                    false
                )
                else if (users != null && users.isNotEmpty()) viewModel?.favoritesAdapter?.addUsers(users)
            }
        })
    }

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

    // Not Working
    private fun setSuggestionsAdapter()
    {
        val lastSuggestions = viewModel?.usersAdapter?.users?.map { it.firstName }?.toTypedArray()
        val suggestionsAdapter = SimpleCursorAdapter(activity, android.R.layout.simple_list_item_1, null, lastSuggestions,
        intArrayOf(android.R.id.text1), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        viewModel?.usersSuggestionsAdapter = suggestionsAdapter

        searchView?.suggestionsAdapter = viewModel?.usersSuggestionsAdapter
        searchView?.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                return false
            }

        })
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView?.queryHint = getString(R.string.search_users_title)
        searchView?.isQueryRefinementEnabled = true
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel?.usersAdapter?.filter?.filter(query)
                viewModel?.favoritesAdapter?.filter?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchView?.suggestionsAdapter?.filter?.filter(newText)
                viewModel?.usersAdapter?.filter?.filter(newText)
                viewModel?.favoritesAdapter?.filter?.filter(newText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

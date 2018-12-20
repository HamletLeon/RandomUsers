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

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
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
        viewModel.usersAdapter = null
        viewModel.favoritesAdapter = null

        if (viewModel.usersAdapter == null) viewModel.getUsers()
        else if (binding.allList?.adapter == null) binding.allList?.adapter = viewModel.usersAdapter
    }

    private fun setListeners() {
        viewModel.users.observe(this, Observer {
            if (binding.allList?.adapter == null) setAdapter(it)
            else viewModel.usersAdapter?.addUsers(it)
        })

        viewModel.suggestions.observe(this, Observer {
            if (viewModel.usersSuggestionsAdapter == null) setSuggestionsAdapter(it)
            else {
//                viewModel.usersSuggestionsAdapter
            }
        })
    }

    private fun setAdapter(users: List<User>, mainAdapter: Boolean = true) {
        if (mainAdapter)
        {
            val (manager, adapter) = setAdapter(binding.allList, users)
            val scrollListener = viewModel.scrollListener(manager)
            binding.allList?.addOnScrollListener(scrollListener)
            viewModel.usersAdapter = adapter
        } else {
            val (_, adapter) = setAdapter(binding.favoritesList, users)
            viewModel.favoritesAdapter = adapter
            binding.favoritesLayout?.visibility = View.VISIBLE
        }
    }

    private fun setAdapter(recyclerView: RecyclerView?, users: List<User>): Pair<GridLayoutManager, UsersAdapter<MainFragment>> {
        val manager = initManager(recyclerView)
        val adapter = UsersAdapter(this, users)
        recyclerView?.adapter = adapter

        return Pair(manager, adapter)
    }
    private fun initManager(recyclerView: RecyclerView?, itemSizeDpHeight: Int = 80, itemSizeDpWidth: Int = 80): GridLayoutManager {
        val (_, itemsOnScreen) = activity.calculateScreenSizeAndItemsOnIt(itemSizeDpHeight, itemSizeDpWidth)
        val manager = recyclerView?.layoutManager as GridLayoutManager
        manager.spanCount = if (viewModel.twoPane.value == true) (itemsOnScreen.itemsOnWidth / 2) else itemsOnScreen.itemsOnWidth
        return manager
    }

    private fun setSuggestionsAdapter(suggestions: List<String>)
    {
        val suggestionsAdapter = SimpleCursorAdapter(activity, android.R.layout.simple_list_item_1, null, suggestions.toTypedArray(),
            intArrayOf(android.R.id.text1), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        viewModel.usersSuggestionsAdapter = suggestionsAdapter
    }
}

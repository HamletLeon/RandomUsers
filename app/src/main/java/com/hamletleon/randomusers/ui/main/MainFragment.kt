package com.hamletleon.randomusers.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.randomusers.R
import com.hamletleon.randomusers.databinding.MainFragmentBinding
import com.hamletleon.randomusers.dtos.UserDto
import com.hamletleon.randomusers.ui.main.adapters.UsersAdapter
import com.hamletleon.randomusers.utils.calculateScreenSizeAndItemsOnIt

class MainFragment : androidx.fragment.app.Fragment() {
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
    }

    private fun setListeners() { }

    private fun setAdapter(mainAdapter: Boolean = true) {
        if (mainAdapter)
        {
            initManager(binding.allList)
            val adapter = UsersAdapter(this, viewModel.users)
            viewModel.usersAdapter = adapter
            binding.allList?.adapter = adapter
        }
        else {
            initManager(binding.favoritesList)
            val adapter = UsersAdapter(this, viewModel.favorites)
            viewModel.favoritesAdapter = adapter
            binding.favoritesList?.adapter = adapter
            binding.favoritesLayout?.visibility = View.VISIBLE
        }
    }
    private fun initManager(recyclerView: RecyclerView?, itemSizeDpHeight: Int = 80, itemSizeDpWidth: Int = 80){
        val (_, itemsOnScreen) = activity.calculateScreenSizeAndItemsOnIt(itemSizeDpHeight, itemSizeDpWidth)
        val manager = recyclerView?.layoutManager as? GridLayoutManager
        manager?.spanCount = if (viewModel.twoPane.value == true) (itemsOnScreen.itemsOnWidth / 2) else itemsOnScreen.itemsOnWidth
    }
}

package com.hamletleon.randomusers.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.hamletleon.randomusers.R
import com.hamletleon.randomusers.databinding.MainFragmentBinding

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

    private fun setListeners() {

    }
}

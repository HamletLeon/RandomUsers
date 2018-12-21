package com.hamletleon.randomusers.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hamletleon.randomusers.R
import com.hamletleon.randomusers.databinding.UserDetailsFragmentBinding

class UserDetailsFragment: Fragment() {
    private lateinit var binding: UserDetailsFragmentBinding
    private lateinit var viewModel: UserDetailsViewModel
    private lateinit var arguments: UserDetailsFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_details_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
        binding.model = viewModel
        binding.setLifecycleOwner(this)

        getArguments()?.let {
            arguments = UserDetailsFragmentArgs.fromBundle(it)
            viewModel.getUserDetails(arguments.userId)
        } ?: run {
            // What to do if arguments error...
        }
    }
}
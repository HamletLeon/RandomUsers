package com.hamletleon.randomusers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hamletleon.randomusers.R
import com.hamletleon.randomusers.databinding.EmptyStateFragmentBinding

class EmptyStateFragment : Fragment() {
    private lateinit var binding: EmptyStateFragmentBinding
    private lateinit var arguments: EmptyStateFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.empty_state_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getArguments()?.let {
            arguments = EmptyStateFragmentArgs.fromBundle(it)
            binding.imageResource = if(arguments.image == 0) R.drawable.ic_contacts else arguments.image
            binding.textResource = if (arguments.text == 0) R.string.no_contacts_selected else arguments.text
        }
    }
}
package com.hamletleon.randomusers.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hamletleon.randomusers.MainActivity
import com.hamletleon.randomusers.R
import com.hamletleon.randomusers.databinding.UserDetailsFragmentBinding
import com.hamletleon.randomusers.utils.addContact
import kotlinx.android.synthetic.main.user_details_fragment.*

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

        setListeners()

        getArguments()?.let {
            arguments = UserDetailsFragmentArgs.fromBundle(it)
            viewModel.getUserDetails(arguments.userId)
        } ?: run {
            // What to do if arguments error...
        }
    }

    private fun setListeners() {
        viewModel.loading.observe(this, Observer {
            progressLayout.visibility = if (it == null) View.GONE else View.VISIBLE
            dataLayout.visibility = if (it == null) View.VISIBLE else View.GONE
        })

        viewModel.addToContacts.observe(this, Observer {
            if (it == true) {
                val user = viewModel.user.value ?: return@Observer
                requireActivity().addContact(user.fullName, user.phone, user.email)
            }
        })

        viewModel.contactSaved.observe(this, Observer {
            if (it == true) {
                Toast.makeText(requireContext(), String.format(getString(R.string.contact_saved_successfully), viewModel.user.value?.getName()), Toast.LENGTH_LONG).show()
                MainActivity.updateMainFragmentAdapters()
            }
        })
    }
}
package com.hamletleon.randomusers.ui.users.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.randomusers.R
import com.hamletleon.randomusers.databinding.UserListItemBinding
import com.hamletleon.randomusers.models.User

class UsersAdapter<T>(private val owner: T, initialUsers: List<User>? = null) : RecyclerView.Adapter<UserViewHolder>() where T : LifecycleOwner {
    private lateinit var binding: UserListItemBinding

    private val users: MutableList<User> = initialUsers?.toMutableList() ?: mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.user_list_item, parent, false)
        binding.setLifecycleOwner(owner)
        return UserViewHolder(binding)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user).setOnClickListener {
            val navController = Navigation.findNavController(it)
            val args = Bundle()
            args.putInt("userId", user.id)
            navController.navigate(R.id.userDetailsFragment, args)
        }
    }

    fun addUsers(moreUsers: List<User>) {
        users.addAll(moreUsers)
        notifyDataSetChanged()
    }

    fun clear() {
        users.clear()
    }
}
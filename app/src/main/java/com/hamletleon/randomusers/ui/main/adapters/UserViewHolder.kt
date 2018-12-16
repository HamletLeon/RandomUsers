package com.hamletleon.randomusers.ui.main.adapters

import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.randomusers.databinding.UserListItemBinding
import com.hamletleon.randomusers.models.User

class UserViewHolder(private val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(user: User)
    {
        binding.model = user
    }
}
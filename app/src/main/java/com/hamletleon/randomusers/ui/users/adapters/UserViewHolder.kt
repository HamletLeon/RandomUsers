package com.hamletleon.randomusers.ui.users.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hamletleon.randomusers.databinding.UserListItemBinding
import com.hamletleon.randomusers.models.User

class UserViewHolder(private val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(user: User): View {
        Glide.with(itemView).load(user.tinyPicture).into(binding.picture)
        binding.model = user

        return itemView
    }
}
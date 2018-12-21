package com.hamletleon.randomusers.ui.users.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.randomusers.R
import com.hamletleon.randomusers.databinding.UserListItemBinding
import com.hamletleon.randomusers.models.User

class UsersAdapter(private val owner: Fragment, initialUsers: List<User>? = null, private val twoPane: Boolean = false) : RecyclerView.Adapter<UserViewHolder>(), Filterable {

    private lateinit var binding: UserListItemBinding

    var filtered = false
    val users: MutableList<User> = initialUsers?.toMutableList() ?: mutableListOf()
    private var filteredUsers: MutableList<User> = initialUsers?.toMutableList() ?: mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.user_list_item, parent, false)
        binding.setLifecycleOwner(owner)
        return UserViewHolder(binding)
    }

    override fun getItemCount() = filteredUsers.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredUsers[position]
        holder.bind(user).setOnClickListener {
            val args = Bundle()
            args.putInt("userId", user.id)
            if (twoPane) {
                val navController = Navigation.findNavController(owner.requireActivity(), R.id.two_pane_nav_fragment)
                navController.navigate(R.id.userDetailsFragment2, args)
            } else {
                val navController = Navigation.findNavController(it)
                navController.navigate(R.id.userDetailsFragment, args)
            }
        }
    }

    fun addUsers(moreUsers: List<User>) {
        users.addAll(moreUsers)
        filteredUsers.addAll(moreUsers)
        notifyDataSetChanged()
    }

    fun clear() {
        users.clear()
        filteredUsers.clear()
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val str = constraint.toString().toLowerCase()
                filteredUsers = if (str.isEmpty()){
                    filtered = false
                    users
                }
                else {
                    filtered = true
                    users.filter { it.firstName.toLowerCase().contains(str) }.toMutableList()
                }

                val results = FilterResults()
                results.values = filteredUsers
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredUsers = results?.values as? MutableList<User> ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }
}
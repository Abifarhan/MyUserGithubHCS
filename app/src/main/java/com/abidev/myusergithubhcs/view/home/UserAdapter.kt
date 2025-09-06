package com.abidev.myusergithubhcs.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abidev.domain.home.usecase.UserDomainModel
import com.abidev.myusergithubhcs.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class UserAdapter(
    private val onUserClick: (UserDomainModel) -> Unit
) : ListAdapter<UserDomainModel, UserAdapter.UserViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position), onUserClick)
    }

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserDomainModel, onUserClick: (UserDomainModel) -> Unit) {
            binding.username.text = user.username
            Glide.with(binding.avatar).load(user.avatarUrl).circleCrop().into(binding.avatar)

            binding.root.setOnClickListener { onUserClick(user) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UserDomainModel>() {
        override fun areItemsTheSame(oldItem: UserDomainModel, newItem: UserDomainModel): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: UserDomainModel, newItem: UserDomainModel): Boolean = oldItem == newItem
    }
}
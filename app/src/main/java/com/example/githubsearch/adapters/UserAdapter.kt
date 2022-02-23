package com.example.githubsearch.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearch.data.model.SearchDataResponse
import com.example.githubsearch.databinding.ListItemUserBinding
import com.example.githubsearch.ui.GithubUserDetailActivity

class UserAdapter : PagingDataAdapter<SearchDataResponse.Items, UserAdapter.UserViewHolder>(GalleryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ListItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    class UserViewHolder(
        private val binding: ListItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.user?.let { user ->
                    val intent = Intent(view.context, GithubUserDetailActivity::class.java).apply {
                        putExtra("Avtar", user.avatar_url)
                        putExtra("Name", user.login)
                    }
                    view.context.startActivity(intent)
                }
            }
        }

        fun bind(item: SearchDataResponse.Items) {
            binding.apply {
                user = item
                executePendingBindings()
            }
        }
    }
}

private class GalleryDiffCallback : DiffUtil.ItemCallback<SearchDataResponse.Items>() {
    override fun areItemsTheSame(oldItem: SearchDataResponse.Items, newItem: SearchDataResponse.Items): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchDataResponse.Items, newItem: SearchDataResponse.Items): Boolean {
        return oldItem == newItem
    }
}
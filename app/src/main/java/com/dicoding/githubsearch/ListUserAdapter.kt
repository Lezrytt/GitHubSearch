package com.dicoding.githubsearch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubsearch.database.Favorite
import com.dicoding.githubsearch.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUser: List<User>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (name, photo) = listUser[position]
        Glide.with(viewHolder.itemView.context)
            .load(photo)
            .into(viewHolder.binding.imgPhoto)
        viewHolder.binding.tvItem.text = name
        Log.e(TAG, "onBindViewHolder: login")
        viewHolder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[viewHolder.adapterPosition]) }
    }

    override fun getItemCount() = listUser.size

    companion object {
        private const val TAG = "ListUserAdapter"
    }
}

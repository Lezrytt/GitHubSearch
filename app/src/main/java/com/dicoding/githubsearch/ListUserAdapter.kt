package com.dicoding.githubsearch

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubsearch.database.Favorite
import com.dicoding.githubsearch.databinding.ItemRowUserBinding
import com.dicoding.githubsearch.helper.FavoriteDiffCallBack

class ListUserAdapter(private val listUser: List<User>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listFavorites = ArrayList<Favorite>()

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setListFavorites(listFavorites: List<Favorite>) {
        val diffCallBack = FavoriteDiffCallBack(this.listFavorites, listFavorites)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        this.listFavorites.clear()
        this.listFavorites.addAll(listFavorites)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user:User) {
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                val favorite = Favorite()
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                intent.putExtra(DetailActivity.EXTRA_FAVORITE, favorite)
                it.context.startActivity(intent)
            }
        }
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
        viewHolder.bind(listUser[position])
//        viewHolder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[viewHolder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    companion object {
        private const val TAG = "ListUserAdapter"
    }
}

package com.dicoding.githubsearch.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsearch.*
import com.dicoding.githubsearch.database.Favorite
import com.dicoding.githubsearch.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listUser: ArrayList<User> = ArrayList()

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorites().observe(this, { favoriteList ->
            if (favoriteList != null) {
                adapter.setListFavorites(favoriteList)
            }
        })

        supportActionBar?.title = getString(R.string.favorites)

        adapter = FavoriteAdapter(listUser)

        binding?.rvUser?.layoutManager = LinearLayoutManager(this)
        binding?.rvUser?.setHasFixedSize(true)
        binding?.rvUser?.adapter = adapter


        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data, favorite = Favorite())
            }
        })
    }


    private fun showSelectedUser(user: User, favorite: Favorite) {
        val moveWithObjectIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_USER, user)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_FAVORITE, favorite)
        startActivity(moveWithObjectIntent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory2.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

}
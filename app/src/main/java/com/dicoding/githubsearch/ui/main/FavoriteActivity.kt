package com.dicoding.githubsearch.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsearch.ListUserAdapter
import com.dicoding.githubsearch.R
import com.dicoding.githubsearch.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorites().observe(this, { favoriteList ->
            if (favoriteList != null) {
                adapter.setListFavorites(favoriteList)
            }
        })

        supportActionBar?.title = getString(R.string.favorites)

        adapter = FavoriteAdapter()

        binding?.rvUser?.layoutManager = LinearLayoutManager(this)
        binding?.rvUser?.setHasFixedSize(true)
        binding?.rvUser?.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory2.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}
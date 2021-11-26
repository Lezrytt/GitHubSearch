package com.dicoding.githubsearch.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubsearch.database.Favorite
import com.dicoding.githubsearch.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorites()

}
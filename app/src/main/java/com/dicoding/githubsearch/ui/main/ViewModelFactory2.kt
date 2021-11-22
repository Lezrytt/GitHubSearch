package com.dicoding.githubsearch.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubsearch.ui.insert.FavoriteAddUpdateViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory2 private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory2? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory2 {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory2::class.java) {
                    INSTANCE = ViewModelFactory2(application)
                }
            }
            return INSTANCE as ViewModelFactory2
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteAddUpdateViewModel::class.java)) {
            return FavoriteAddUpdateViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}
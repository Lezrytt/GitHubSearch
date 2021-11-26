package com.dicoding.githubsearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsearch.*
import com.dicoding.githubsearch.database.Favorite
import com.dicoding.githubsearch.databinding.ActivityMainBinding
import com.dicoding.githubsearch.ui.insert.FavoriteAddUpdateViewModel
import com.dicoding.githubsearch.ui.main.FavoriteActivity
import com.dicoding.githubsearch.ui.main.FavoriteAdapter
import com.dicoding.githubsearch.ui.main.FavoriteViewModel
import com.dicoding.githubsearch.ui.main.ViewModelFactory2
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListUserAdapter

    private var favorite: Favorite? = Favorite()
    private val listUser: ArrayList<User> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        val favoriteViewModel = obtainViewModel(this@MainActivity)
        favoriteViewModel.getAllFavorites().observe(this, { favoriteList ->
            if (favoriteList != null) {
                adapter = ListUserAdapter(listUser)
                adapter.setListFavorites(favoriteList)
            }
        })

        showLoading(false)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        binding.rvUser.layoutManager = layoutManager

        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.listUser.observe(this, { searchResult ->
            setSearchData(searchResult)
        })

        mainViewModel.snackbarText.observe(this, {
            Snackbar.make(
                window.decorView.rootView,
                it,
                Snackbar.LENGTH_LONG
            ).show()
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsMenu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.favoritesMenu -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    private fun showSelectedUser(user: User, favorite: Favorite) {
        val moveWithObjectIntent = Intent(this@MainActivity, DetailActivity::class.java)

        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_USER, user)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_FAVORITE, favorite)

        startActivity(moveWithObjectIntent)
        detailViewModel = DetailViewModel()
    }

    private fun setSearchData(searchResult: List<ItemsItem>) {
        for (user in searchResult) {
            val userList = User(user.login, user.avatarUrl, favorite?.isFavorite)
            listUser.add(userList)
        }

        adapter = ListUserAdapter(listUser)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data, favorite = Favorite())
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory2.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
    }
}

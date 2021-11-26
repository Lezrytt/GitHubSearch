package com.dicoding.githubsearch

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubsearch.database.Favorite
import com.dicoding.githubsearch.databinding.ActivityDetailBinding
import com.dicoding.githubsearch.ui.insert.FavoriteAddUpdateViewModel
import com.dicoding.githubsearch.ui.main.ViewModelFactory2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel

    private var favorite: Favorite? = Favorite()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        val actionBar = supportActionBar
        val sectionsPagerAdapter = SectionsPagerAdapter(this, user.User.toString())

        actionBar?.title = "User Information"

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]

        user.User?.let { detailViewModel.findUserDetail(it) }

        detailViewModel.detailUser.observe(this, {
            detailUser -> setDetailData(detailUser)
        })

        detailViewModel.listFollowing.observe(this, {
            listFollowing -> setFollowingData(listFollowing)
        })

        detailViewModel.snackbarText.observe(this, {
            Snackbar.make(
                window.decorView.rootView,
                it,
                Snackbar.LENGTH_LONG
            ).show()
        })

        binding.tvUserName.text = user.User
        Glide.with(this@DetailActivity)
            .load(user.Photo)
            .into(binding.imgDetailPhoto)

        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        actionBar?.elevation = 0f

        favoriteAddUpdateViewModel = obtainViewModel(this@DetailActivity)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        favorite = intent.getParcelableExtra(EXTRA_FAVORITE)

        val avatar = user.Photo

        var isFavorite = false
        var check: Boolean? = favorite?.isFavorite
        var checkAva: String? = favorite?.avatar
        var checkUse: String? = favorite?.username
        var checkFavUser: Boolean? = user.isFavorite

        Log.e("DetailActivityCheck", "isFavorite: $check and $checkFavUser, ava: $checkAva, user: $checkUse")
        if(favorite?.isFavorite == true) {
            isFavorite = true
        } else if (favorite?.isFavorite == false) {
            isFavorite = false
        } else if (user.isFavorite == true) {
            isFavorite = true
        }

        when (item.itemId) {
            R.id.favoritesMenu -> {
                if (!isFavorite) {
                    favorite.let { favorite ->
                        favorite?.username = user.User!!
                        favorite?.avatar = avatar
                        favorite?.isFavorite = true
                    }
                    favoriteAddUpdateViewModel.insert(favorite as Favorite)
                    user.isFavorite = true
                    Toast.makeText(this, "Ditambahkan ke favorit!", Toast.LENGTH_SHORT).show()

                } else if (isFavorite) {
                    favorite.let { favorite ->
                        favorite?.isFavorite = false
                    }
                    favoriteAddUpdateViewModel.delete(favorite as Favorite)
                    user.isFavorite = false
                    Toast.makeText(this, "Dihapus dari favorit!", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else -> return true
        }
    }

    private fun setDetailData(detailUser: DetailResponse) {
        binding.tvFollowing.text = detailUser.following.toString()
        binding.tvFollowers.text = detailUser.followers.toString()

        if (detailUser.name == null) {
            binding.tvFullName.text = detailUser.login
        } else {
            binding.tvFullName.text = detailUser.name
        }

        binding.tvCompany.text = detailUser.company

        val mFollowingFragment = FollowingFragment()
        val mFollowersFragment = FollowerFragment()
        val mBundle = Bundle()
        val mBundle2 = Bundle()
        mBundle.putString(FollowerFragment.EXTRA_USER, detailUser.login)
        mBundle2.putString(FollowingFragment.EXTRA_USER, detailUser.login)
        mFollowingFragment.arguments = mBundle
        mFollowersFragment.arguments = mBundle2

        Log.e(ContentValues.TAG, "showSelectedUser: ${detailUser.login}")

    }

    private fun setFollowingData(listFollowing: List<FollowingResponseItem>) {
        val listUser: ArrayList<User> = ArrayList()
        for (user in listFollowing) {
            val userList = User(user.login, user.avatarUrl)
            listUser.add(userList)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
        val factory = ViewModelFactory2.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteAddUpdateViewModel::class.java]
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"
    }
}
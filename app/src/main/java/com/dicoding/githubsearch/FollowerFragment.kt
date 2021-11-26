package com.dicoding.githubsearch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsearch.database.Favorite
import com.dicoding.githubsearch.databinding.FragmentFollowerBinding


class FollowerFragment : Fragment() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: FragmentFollowerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        binding.rvFollower.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getString(DetailActivity.EXTRA_USER)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        if (user != null) {
            detailViewModel.findFollowers(user)
        }
        detailViewModel.listFollowers.observe(viewLifecycleOwner, {
            listFollowers -> setFollowersData(listFollowers)
            showLoading(false)
        })
        detailViewModel.isLoading.observe(viewLifecycleOwner, {
        showLoading(it)
        })
    }
    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setFollowersData(listFollowers: List<FollowersResponseItem>) {
        val listUser: ArrayList<User> = ArrayList()
        showLoading(true)
        for (user in listFollowers) {
            val userList = User(user.login, user.avatarUrl)
            listUser.add(userList)
        }
        adapter = ListUserAdapter(listUser)
        binding.rvFollower.setHasFixedSize(true)
        binding.rvFollower.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data, favorite = Favorite())
            }
        })
    }

    private fun showSelectedUser(user: User, favorite: Favorite) {
        val moveWithObjectIntent = Intent(activity, DetailActivity::class.java)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_USER, user)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_FAVORITE, favorite)
        startActivity(moveWithObjectIntent)
        detailViewModel = DetailViewModel()
    }

    companion object {
        const val EXTRA_USER ="extra_user"
        @JvmStatic
        fun newInstance(string: String) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_USER, string)
                }
            }
    }
}
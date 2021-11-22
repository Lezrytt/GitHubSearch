package com.dicoding.githubsearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser: LiveData<DetailResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listFollowers = MutableLiveData<List<FollowersResponseItem>>()
    val listFollowers: LiveData<List<FollowersResponseItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<FollowingResponseItem>>()
    val listFollowing: LiveData<List<FollowingResponseItem>> = _listFollowing

    private val _snackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String> = _snackbarText

    fun findUserDetail(detailQuery: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetail(detailQuery)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailUser.value = responseBody
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = "Loading failed, please check your connection!"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun findFollowers(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(user)
        client.enqueue(object : Callback<List<FollowersResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowersResponseItem>>,
                response: Response<List<FollowersResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = "Loading failed, please check your connection!"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun findFollowing(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(user)
        client.enqueue(object : Callback<List<FollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowingResponseItem>>,
                response: Response<List<FollowingResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = "Loading failed, please check your connection!"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}
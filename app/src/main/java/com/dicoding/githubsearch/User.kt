package com.dicoding.githubsearch

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var User: String? = null,
    var Photo: String? = null
): Parcelable

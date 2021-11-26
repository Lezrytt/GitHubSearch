package com.dicoding.githubsearch.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Favorite(
/*
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
*/
    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "avatar")
    var avatar: String? = null,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = false

): Parcelable

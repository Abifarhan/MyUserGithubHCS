package com.abidev.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val login: String,
    val avatarUrl: String,
    val bio: String?,
    val name: String?,
    val publicRepos: Int,
    val followers: Int,
    val following: Int
)
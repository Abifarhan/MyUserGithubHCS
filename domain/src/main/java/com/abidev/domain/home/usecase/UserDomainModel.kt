package com.abidev.domain.home.usecase

data class UserDomainModel(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val bio: String?,
    val name: String?,
    val publicRepos: Int,
    val followers: Int,
    val following: Int
)
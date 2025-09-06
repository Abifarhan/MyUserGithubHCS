package com.abidev.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponseDto(
    @Json(name = "items") val items: List<UserDto>?
)

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Int?,
    val login: String?,
    @Json(name = "avatar_url") val avatarUrl: String?
)

@JsonClass(generateAdapter = true)
data class UserDetailDto(
    val id: Int?,
    val login: String?,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "bio") val bio: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "public_repos") val publicRepos: Int = 0,
    @Json(name = "followers") val followers: Int = 0,
    @Json(name = "following") val following: Int = 0,
    @Json(name = "html_url") val htmlUrl: String?
)


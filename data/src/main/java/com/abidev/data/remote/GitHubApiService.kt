package com.abidev.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): SearchResponseDto

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("users/{login}")
    suspend fun getUserDetail(@Path("login") login: String): UserDetailDto
}
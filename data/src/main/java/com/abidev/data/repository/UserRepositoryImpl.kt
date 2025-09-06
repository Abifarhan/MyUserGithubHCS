package com.abidev.data.repository

import com.abidev.data.local.UserDao
import com.abidev.data.local.UserEntity
import com.abidev.data.remote.GitHubApiService
import com.abidev.data.remote.SearchResponseDto
import com.abidev.data.remote.UserDetailDto
import com.abidev.data.remote.UserDto
import com.abidev.domain.UserRepository
import com.abidev.domain.home.usecase.UserDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.text.isBlank
import kotlin.text.orEmpty

class UserRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService,
    private val dao: UserDao
) : UserRepository {

    override suspend fun searchUsers(query: String): List<UserDomainModel> =
        withContext(Dispatchers.IO) {
            if (query.isBlank()) return@withContext emptyList()

            val cached = dao.searchUsers(query).first()
            if (cached.isNotEmpty()) {
                return@withContext cached.map { it.toDomainEntity() }
            }

            val response = apiService.searchUsers(query)
            val users = response.items?.map { it.toDomain() } ?: emptyList()

            dao.insertAll(users.map { it.toEntity() })

            users
        }

    override suspend fun getDefaultUsers(): List<UserDomainModel> = withContext(Dispatchers.IO) {
        val cached = dao.observeAllLimited(50).first()
        if (cached.isNotEmpty()) return@withContext cached.map { it.toDomain() }

        val remote = apiService.getUsers()
        val domain = remote.map { it.toDomain() }

        dao.insertAll(remote.map { it.toEntity() })

        domain
    }

    override suspend fun getUserDetail(username: String): UserDomainModel =
        withContext(Dispatchers.IO) {
            try {
                val userDto = apiService.getUserDetail(username)
                val domainModel = userDto.toDomain()

                dao.insert(userDto.toEntity())

                domainModel
            } catch (e: Exception) {
                val cached = dao.getUserByUsername(username)
                if (cached != null) {
                    cached.toDomain()
                } else {
                    throw kotlin.Exception(
                        "Gagal mengambil data dari API dan tidak ada data lokal",
                        e
                    )
                }
            }
        }
}

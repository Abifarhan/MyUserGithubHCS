package com.abidev.data.repository

import com.abidev.data.local.UserEntity
import com.abidev.data.remote.SearchResponseDto
import com.abidev.data.remote.UserDetailDto
import com.abidev.data.remote.UserDto
import com.abidev.domain.home.usecase.UserDomainModel


fun UserDetailDto.toEntity(): UserEntity {
    return UserEntity(
        id = id ?: -1,
        login = login.orEmpty(),
        avatarUrl = avatarUrl.orEmpty(),
        bio = bio,
        name = name,
        publicRepos = publicRepos,
        followers = followers,
        following = following
    )
}


fun UserEntity.toDomain(): UserDomainModel {
    return UserDomainModel(
        id = id,
        username = login,
        avatarUrl = avatarUrl,
        bio = bio,
        name = name,
        publicRepos = publicRepos,
        followers = followers,
        following = following
    )
}


fun UserDomainModel.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        login = username,
        avatarUrl = avatarUrl,
        bio = bio,
        name = name,
        publicRepos = publicRepos,
        followers = followers,
        following = following
    )
}

fun UserEntity.toDomainEntity(): UserDomainModel {
    return UserDomainModel(
        id = id,
        username = login,
        avatarUrl = avatarUrl,
        bio = bio,
        name = name,
        publicRepos = publicRepos,
        followers = followers,
        following = following
    )
}


fun UserDto.toDomain(): UserDomainModel {
    return UserDomainModel(
        id = id ?: -1,
        username = login.orEmpty(),
        avatarUrl = avatarUrl.orEmpty(),
        bio = null,
        name = null,
        publicRepos = 0,
        followers = 0,
        following = 0
    )
}


fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id ?: -1,
        login = login.orEmpty(),
        avatarUrl = avatarUrl.orEmpty(),
        bio = null,
        name = null,
        publicRepos = 0,
        followers = 0,
        following = 0
    )
}

fun SearchResponseDto.toEntityList(): List<UserEntity> {
    return items?.map { it.toEntity() } ?: emptyList()
}

fun UserDetailDto.toDomain(): UserDomainModel {
    return UserDomainModel(
        id = id ?: -1,
        username = login.orEmpty(),
        avatarUrl = avatarUrl.orEmpty(),
        bio = bio,
        name = name,
        publicRepos = publicRepos,
        followers = followers,
        following = following
    )
}
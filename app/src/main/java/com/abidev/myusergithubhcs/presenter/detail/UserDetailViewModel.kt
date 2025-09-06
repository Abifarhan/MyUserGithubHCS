package com.abidev.myusergithubhcs.presenter.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abidev.core.Resource
import com.abidev.domain.detail.usecase.GetUserDetailUseCase
import com.abidev.domain.home.usecase.UserDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<Resource<UserDomainModel>>()
    val uiState: LiveData<Resource<UserDomainModel>> = _uiState

    fun getUserDetail(username: String) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            try {
                val user = getUserDetailUseCase.execute(username)
                _uiState.value = Resource.Success(user)
            } catch (e: Exception) {
                _uiState.value = Resource.Error(e.message ?: "Gagal memuat detail user")
            }
        }
    }
}
package com.abidev.myusergithubhcs.presenter.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchUsersUseCase: GetUsersUseCase,
    private val getDefaultUsersUseCase: GetDefaultUsersUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<Resource<List<UserDomainModel>>>()
    val uiState: LiveData<Resource<List<UserDomainModel>>> = _uiState

    init {
        loadDefaultUsers()
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            try {
                val result = searchUsersUseCase.execute(query)
                _uiState.value = Resource.Success(result)
            } catch (e: Exception) {
                _uiState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }


    fun loadDefaultUsers() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            try {
                val users = getDefaultUsersUseCase.execute()
                _uiState.value = Resource.Success(users)
            } catch (e: Exception) {
                _uiState.value = Resource.Error(e.message ?: "Gagal memuat data awal")
            }
        }
    }
}
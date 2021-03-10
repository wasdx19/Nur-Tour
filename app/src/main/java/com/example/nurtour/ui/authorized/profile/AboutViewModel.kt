package com.example.nurtour.ui.authorized.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.data.model.UserDTO
import com.example.nurtour.domain.GetUserNameUseCase
import com.example.nurtour.domain.MakeUserLogoutUseCase

class AboutViewModel(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val makeUserLogoutUseCase: MakeUserLogoutUseCase
) : ViewModel(){

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun onViewCreated(){
        getUserNameUseCase.request(Unit, onResult = {
            val user = it.toObject(UserDTO::class.java)
            _name.value = user?.name.orEmpty()
        },loading = _loading, error = _error)
    }

    fun logout(){
        makeUserLogoutUseCase.logout()
    }
}
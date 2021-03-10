package com.example.nurtour.ui.unauthorized.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.domain.GetMakeAuthUseCase

class LoginViewModel(
    private val getMakeAuthUseCase: GetMakeAuthUseCase
): ViewModel(){

    private val _authorized = MutableLiveData<Unit>()
    val authorized: LiveData<Unit>
        get() = _authorized

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun makeAuthorization(email:String, password: String){
        getMakeAuthUseCase.request(Pair(email, password), onResult = {
            _authorized.value = Unit
        },loading = _loading, error = _error)
    }

}
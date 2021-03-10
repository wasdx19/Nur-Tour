package com.example.nurtour.ui.unauthorized.registry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.domain.GetMakeRegistrationUseCase

class RegistryViewModel(
    private val makeRegistrationUseCase: GetMakeRegistrationUseCase
) : ViewModel() {

    private val _registred = MutableLiveData<String>()
    val registred: LiveData<String>
        get() = _registred

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun makeRegistration(email: String, password: String, name: String) {
        makeRegistrationUseCase.request(Pair(email, password), onResult = {
            _registred.value = name
        }, loading = _loading, error = _error)
    }
}
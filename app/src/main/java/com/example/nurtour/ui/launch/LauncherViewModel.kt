package com.example.nurtour.ui.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.domain.GetIsAuthorizedUseCase

class LauncherViewModel(
    private val getIsAuthorizedUseCase: GetIsAuthorizedUseCase
) : ViewModel(){

    private val _userExist = MutableLiveData<Unit>()
    val userExist: LiveData<Unit>
        get() = _userExist

    private val _userNotExist = MutableLiveData<Unit>()
    val userNotExist: LiveData<Unit>
        get() = _userNotExist

    fun onCreate(){
        if(getIsAuthorizedUseCase.isUserAuthorized())
            _userExist.value = Unit
        else
            _userNotExist.value = Unit
    }
}
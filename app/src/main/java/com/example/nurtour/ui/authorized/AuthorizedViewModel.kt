package com.example.nurtour.ui.authorized

import androidx.lifecycle.ViewModel
import com.example.nurtour.domain.AddUserUseCase

class AuthorizedViewModel(
    private val addUserUseCase: AddUserUseCase
) : ViewModel(){

    fun saveUser(name:String?){
        if(!name.isNullOrEmpty()) {
            addUserUseCase.request(name, onResult = {})
        }
    }

}
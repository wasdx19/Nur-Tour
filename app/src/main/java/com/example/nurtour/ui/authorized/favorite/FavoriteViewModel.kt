package com.example.nurtour.ui.authorized.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.domain.GetUserFavsUseCase

class FavoriteViewModel(
    private val userFavsUseCase: GetUserFavsUseCase
) : ViewModel(){

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _favPlaces = MutableLiveData<List<Any>>()
    val favPlaces : LiveData<List<Any>>
        get() = _favPlaces

    fun onViewCreated(){
        userFavsUseCase.request(Unit, onResult = {
            val list = arrayListOf<Any>()
            it.forEach {
                it?.toObject(PlaceDTO::class.java)?.let { it1 -> list.add(it1) }
            }
            _favPlaces.value = list
        }, loading = _loading, error = _error)
    }
}
package com.example.nurtour.ui.authorized.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.domain.GetAllPlacesUseCase

class MapViewModel(
    private val getAllPlacesUseCase: GetAllPlacesUseCase
) : ViewModel(){

    private val _place = MutableLiveData<List<PlaceDTO>>()
    val place: LiveData<List<PlaceDTO>>
        get() = _place

    fun onViewCreated(){
        getAllPlacesUseCase.request(Unit, onResult = {
            val items = arrayListOf<PlaceDTO>()
            it.forEach {
                it?.toObject(PlaceDTO::class.java)?.let { it1 -> items.add(it1) }
            }
            _place.value = items
        })
    }

}
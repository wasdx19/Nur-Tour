package com.example.nurtour.ui.authorized.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.data.model.PlaceTypeDTO
import com.example.nurtour.domain.GetAllPlacesUseCase
import com.example.nurtour.domain.GetPlaceTypeUseCase
import com.example.nurtour.domain.GetPlacesByTypeUseCase

class MainViewModel(
    private val getPlaceTypeUseCase: GetPlaceTypeUseCase,
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val getPlacesByTypeUseCase: GetPlacesByTypeUseCase
) : ViewModel() {

    private val _placeTypes = MutableLiveData<List<Any>>()
    val placeTypes: LiveData<List<Any>>
        get() = _placeTypes

    private val _place = MutableLiveData<List<Any>>()
    val place: LiveData<List<Any>>
        get() = _place

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var type = 0

    fun onViewCreated() {
        getPlaceTypes()
        onTypeClicked(type)
    }

    fun onTypeClicked(typeId: Int) {
        type = typeId
        when (type) {
            0 -> getAllPlaces()
            else -> getPlacesByType(typeId)
        }
    }

    private fun getPlaceTypes() {
        getPlaceTypeUseCase.request(Unit, onResult = {
            val items = arrayListOf<PlaceTypeDTO>()
            it.forEach {
                it?.toObject(PlaceTypeDTO::class.java)?.let { it1 -> items.add(it1) }
            }
            _placeTypes.value = items
        })
    }

    private fun getAllPlaces() {
        getAllPlacesUseCase.request(Unit, onResult = {
            val items = arrayListOf<PlaceDTO>()
            it.forEach {
                it?.toObject(PlaceDTO::class.java)?.let { it1 -> items.add(it1) }
            }
            _place.value = items
        }, loading = _loading, error = _error)
    }

    private fun getPlacesByType(type: Int) {
        getPlacesByTypeUseCase.request(type, onResult = {
            val items = arrayListOf<PlaceDTO>()
            it.forEach {
                it?.toObject(PlaceDTO::class.java)?.let { it1 -> items.add(it1) }
            }
            _place.value = items
        }, loading = _loading, error = _error)
    }
}
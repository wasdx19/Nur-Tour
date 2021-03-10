package com.example.nurtour.ui.authorized.placeDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.data.model.NoteDTO
import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.domain.AddUserFavoriteUseCase
import com.example.nurtour.domain.GetPublicNotesByPlace
import com.example.nurtour.domain.IsUserFavUseCase

class PlaceDetailViewModel(
    private val addUserFavoriteUseCase: AddUserFavoriteUseCase,
    private val isUserFavUseCase: IsUserFavUseCase,
    private val getPublicNotesByPlace: GetPublicNotesByPlace
) : ViewModel(){

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _notes = MutableLiveData<List<Any>>()
    val notes: LiveData<List<Any>>
        get() = _notes

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    fun onViewCreated(id: Int){
        isUserFavUseCase.request(id, onResult = {
            _isFavorite.value = !it.isNullOrEmpty()
        }, loading = _loading, error = _error)

        getPublicNotesByPlace.request(id, onResult = {
            val list = arrayListOf<Any>()
            it.forEach {
                it?.toObject(NoteDTO::class.java)?.let { it1 -> list.add(it1) }
            }
            _notes.value = list
        }, loading = _loading, error = _error)
    }

    fun addUserFavorite(data: PlaceDTO){
        addUserFavoriteUseCase.request(data, onResult = {},loading = _loading, error = _error)
    }
}
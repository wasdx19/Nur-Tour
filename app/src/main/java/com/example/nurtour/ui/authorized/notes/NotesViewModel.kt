package com.example.nurtour.ui.authorized.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.data.model.NoteDTO
import com.example.nurtour.domain.GetAllUserNotesUseCase

class NotesViewModel(
    private val getAllUserNotesUseCase: GetAllUserNotesUseCase
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


    fun onViewCreated(){
        getAllUserNotesUseCase.request(Unit, onResult = {
            val list = arrayListOf<Any>()
            it.forEach {
                it?.toObject(NoteDTO::class.java)?.let { it1 -> list.add(it1) }
            }
            _notes.value = list
        }, loading = _loading, error = _error)
    }

}
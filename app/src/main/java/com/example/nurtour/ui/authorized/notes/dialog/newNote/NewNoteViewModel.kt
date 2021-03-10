package com.example.nurtour.ui.authorized.notes.dialog.newNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nurtour.data.model.NoteDTO
import com.example.nurtour.data.model.UserDTO
import com.example.nurtour.domain.GetUserNameUseCase
import com.example.nurtour.domain.MakeAddNotesUseCase
import com.example.nurtour.domain.MakeAddPublicNotesUseCase

class NewNoteViewModel(
    private val addNotesUseCase: MakeAddNotesUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val makeAddPublicNotesUseCase: MakeAddPublicNotesUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var userName: String = ""

    fun onViewCreated(){
        getUserNameUseCase.request(Unit, onResult = {
            val user = it.toObject(UserDTO::class.java)
            userName = user?.name.orEmpty()
        })
    }

    fun addNoteUseCase(
        title: String,
        description: String,
        placeId: Int,
        isPublic: Boolean,
        imgArray: List<String>
    ) {
        val note = NoteDTO(0, title, description, placeId, isPublic, imgArray, userName)
        addNotesUseCase.request(note, onResult = {}, loading = _loading, error = _error)

        if(isPublic)
            makeAddPublicNotesUseCase.request(note, onResult = {}, loading = _loading, error = _error)
    }

}
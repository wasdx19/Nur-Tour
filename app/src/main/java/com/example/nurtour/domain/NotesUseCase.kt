package com.example.nurtour.domain

import com.example.nurtour.data.model.NoteDTO
import com.example.nurtour.data.repository.NotesRepository
import com.example.nurtour.domain.base.FirestoreDocRefUseCase
import com.example.nurtour.domain.base.FirestoreUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot

/**
 *  UseCase для добавление заметки
 */
class MakeAddNotesUseCase(
    private val repo: NotesRepository
) : FirestoreDocRefUseCase<NoteDTO>() {

    override fun run(params: NoteDTO, response: (Task<DocumentReference>) -> Unit) {
        response.invoke(repo.addNotes(params))
    }
}

/**
 *  UseCase для добавление публичной заметки
 */
class MakeAddPublicNotesUseCase(
    private val repo: NotesRepository
): FirestoreDocRefUseCase<NoteDTO>(){

    override fun run(params: NoteDTO, response: (Task<DocumentReference>) -> Unit) {
        response.invoke(repo.addPublicNotes(params))
    }
}

/**
 *  UseCase для получения всех заметок пользователя
 */
class GetAllUserNotesUseCase(
    private val repo: NotesRepository
) : FirestoreUseCase<Unit>() {

    override fun run(params: Unit, response: (Task<QuerySnapshot>) -> Unit) {
        response.invoke(repo.getUserNotes())
    }
}

/**
 *  UseCase для получения публичных заметок по местам
 */
class GetPublicNotesByPlace(
    private val repo: NotesRepository
) : FirestoreUseCase<Int>() {

    override fun run(params: Int, response: (Task<QuerySnapshot>) -> Unit) {
        response.invoke(repo.getPublicNotesByPlace(params))
    }
}
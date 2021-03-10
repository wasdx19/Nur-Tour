package com.example.nurtour.data.repository

import com.example.nurtour.data.model.NoteDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotesRepository (private val auth: FirebaseAuth, private val db: FirebaseFirestore){

    companion object{
        const val USER_COLLECTION = "user"
        const val USER_NOTES = "notes"
    }

    private val uid = auth.currentUser?.uid

    /**
     *  Добавляем заметку пользователя
     */
    fun addNotes(notes: NoteDTO) =db.collection(USER_COLLECTION).document(uid.orEmpty()).collection(USER_NOTES).add(notes)

    /**
     *  Добавляем публичную заметку пользователя
     */
    fun addPublicNotes(notes: NoteDTO) =db.collection(USER_NOTES).add(notes)

    /**
     *  Получаем публичные заметки по местам
     */
    fun getPublicNotesByPlace(placeId: Int) = db.collection(USER_NOTES).whereEqualTo("public", true).whereEqualTo("placeId", placeId).get()

    /**
     *  Получаем все заметки
     */
    fun getUserNotes() =
        db.collection(USER_COLLECTION).document(uid.orEmpty()).collection(USER_NOTES).get()
}
package com.example.nurtour.data.repository

import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.data.model.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository(val auth: FirebaseAuth, val db: FirebaseFirestore) {

    companion object{
        const val USER_COLLECTION = "user"
        const val USER_FAVORITES = "favorite"
    }

    private val uid = auth.currentUser?.uid

    /**
     * Добавляем в базу нового пользователя
     */
    fun addUser(userName : String) =
        db.collection(USER_COLLECTION).document(uid.orEmpty()).set(UserDTO(uid.orEmpty(), userName))

    /**
     * Добавляем избранное место в базу
     */
    fun userAddFav(place: PlaceDTO) =
        db.collection(USER_COLLECTION).document(uid.orEmpty()).collection(USER_FAVORITES).add(place)

    /**
     * Проверка статус избранное или нет
     */
    fun isFavorite(placeId: Int) =
        db.collection(USER_COLLECTION).document(uid.orEmpty()).collection(USER_FAVORITES).whereEqualTo("id", placeId).get()

    /**
     * Получаем весь список избранных мест
     */
    fun getUserFavorites() =
        db.collection(USER_COLLECTION).document(uid.orEmpty()).collection(USER_FAVORITES).get()

    /**
     *  Получаем имя пользователя
     */
    fun getUserName() = db.collection(USER_COLLECTION).document(uid.orEmpty()).get()

    /**
     *  Выход из аккаунта
     */
    fun logout() = auth.signOut()

}
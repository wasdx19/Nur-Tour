package com.example.nurtour.domain

import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.data.repository.UserRepository
import com.example.nurtour.domain.base.FirestoreDocRefUseCase
import com.example.nurtour.domain.base.FirestoreDocSnapUseCase
import com.example.nurtour.domain.base.FirestoreUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

/**
 *  UseCase для получения избранных мест
 */
class GetUserFavsUseCase(
    private val repo: UserRepository
) : FirestoreUseCase<Unit>() {

    override fun run(params: Unit, response: (Task<QuerySnapshot>) -> Unit) {
        response.invoke(repo.getUserFavorites())
    }
}

/**
 *  UseCase для добавления данных пользователя
 */
class AddUserUseCase(
    private val repo: UserRepository
) : FirestoreUseCase<String>() {

    override fun run(params: String, response: (Task<QuerySnapshot>) -> Unit) {
        repo.addUser(params)
    }
}

/**
 *  UseCase для добавления избранного места пользователя
 */
class AddUserFavoriteUseCase(
    private val repo: UserRepository
) : FirestoreDocRefUseCase<PlaceDTO>() {

    override fun run(params: PlaceDTO, response: (Task<DocumentReference>) -> Unit) {
        response.invoke(repo.userAddFav(params))
    }
}

/**
 *  UseCase для проверки на избранного
 */
class IsUserFavUseCase(
    private val repo: UserRepository
) : FirestoreUseCase<Int>() {
    override fun run(params: Int, response: (Task<QuerySnapshot>) -> Unit) {
        response.invoke(repo.isFavorite(params))
    }
}

/**
 *  UseCase для получения имени пользователя
 */
class GetUserNameUseCase(
    private val repo: UserRepository
) : FirestoreDocSnapUseCase<Unit>(){

    override fun run(params: Unit, response: (Task<DocumentSnapshot>) -> Unit) {
        response.invoke(repo.getUserName())
    }
}

/**
 *  UseCase для выхода с аккаунта
 */
class MakeUserLogoutUseCase(
    private val repo: UserRepository
){
    fun logout(){
        repo.logout()
    }
}
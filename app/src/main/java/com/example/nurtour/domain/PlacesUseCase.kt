package com.example.nurtour.domain

import com.example.nurtour.data.repository.PlacesRepository
import com.example.nurtour.domain.base.FirestoreUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

/**
 *  UseCase для получения вида
 */
class GetPlaceTypeUseCase(private val repo: PlacesRepository) :
    FirestoreUseCase<Unit>() {

    override fun run(params: Unit, response: (Task<QuerySnapshot>) -> Unit) {
        response.invoke(repo.getPlacesType())
    }
}

/**
 *  UseCase для получения всех мест
 */
class GetAllPlacesUseCase(private val repo: PlacesRepository) : FirestoreUseCase<Unit>() {

    override fun run(params: Unit, response: (Task<QuerySnapshot>) -> Unit) {
        response.invoke(repo.getAllPlaces())
    }
}

/**
 *  UseCase для получения мест по фильтру типа
 */
class GetPlacesByTypeUseCase(private val repo: PlacesRepository) : FirestoreUseCase<Int>() {

    override fun run(params: Int, response: (Task<QuerySnapshot>) -> Unit) {
        response.invoke(repo.getPlacesByType(params))
    }
}
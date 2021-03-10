package com.example.nurtour.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PlacesRepository(private val db: FirebaseFirestore) {

    companion object {
        private const val PLACE_TYPE_PATH = "placeType"
        private const val PLACES_PATH = "places"
        private const val TYPE_ID_FIELD = "typeId"
        private const val ID_FIELD = "id"
    }

    /**
     *  Получаем виды мест
     */
    fun getPlacesType() = db.collection(PLACE_TYPE_PATH).orderBy(ID_FIELD, Query.Direction.ASCENDING).get()

    /**
     * Получаем все места
     */
    fun getAllPlaces() = db.collection(PLACES_PATH).orderBy(ID_FIELD, Query.Direction.ASCENDING).get()

    /**
     * Получаем место по виду
     */
    fun getPlacesByType(type: Int) = db.collection(PLACES_PATH).whereEqualTo(TYPE_ID_FIELD, type).get()
}
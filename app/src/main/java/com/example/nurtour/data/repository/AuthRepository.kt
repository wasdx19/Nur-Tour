package com.example.nurtour.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository(private val auth: FirebaseAuth){

    /**
     * Метод для регистра пользователя
     */
    fun makeRegistration(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password)


    /**
     * Метод для авториз. пользователя
     */
    fun makeAuthorization(email: String, password: String) = auth.signInWithEmailAndPassword(email, password)


    /**
     * Возвращает статус пользователя на авторизованность
     */
    fun isUserAuthorized() = auth.currentUser != null

}
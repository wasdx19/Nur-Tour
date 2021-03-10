package com.example.nurtour.domain

import com.example.nurtour.data.repository.AuthRepository
import com.example.nurtour.domain.base.AuthUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.rpc.context.AttributeContext

/**
 *  UseCase для регистра
 */
class GetMakeRegistrationUseCase(
    private val repo: AuthRepository
) : AuthUseCase<Pair<String, String>>() {

    override fun run(params: Pair<String, String>, response: (Task<AuthResult>) -> Unit) {
        val (email, password) = params
        response.invoke(repo.makeRegistration(email, password))
    }
}

/**
 *  UseCase для авториза
 */
class GetMakeAuthUseCase(
    private val repo: AuthRepository
) : AuthUseCase<Pair<String, String>>() {

    override fun run(params: Pair<String, String>, response: (Task<AuthResult>) -> Unit) {
        val (email, password) = params
        response.invoke(repo.makeAuthorization(email, password))
    }
}

/**
 *  UseCase для проверки на авторизованность
 */
class GetIsAuthorizedUseCase(
    private val repo: AuthRepository
) {
    fun isUserAuthorized() = repo.isUserAuthorized()
}
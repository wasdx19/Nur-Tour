package com.example.nurtour.domain.base

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*

abstract class AuthUseCase<in Params> {

    abstract fun run(
        params: Params,
        response: (Task<AuthResult>) -> Unit
    )

    fun request(
        params: Params,
        onResult: (AuthResult?) -> Unit,
        loading: MutableLiveData<Boolean>? = null,
        error: MutableLiveData<String>? = null
    ) {
        loading?.value = true

        run(params) {
            try {
                it.addOnSuccessListener {
                    onResult.invoke(it)
                    loading?.value = false
                }.addOnFailureListener {
                    when(it){
                        is FirebaseNetworkException -> {
                            error?.value = "Проблемы с соединением. Проверьте интернет"
                            loading?.value = false
                        }
                        is FirebaseAuthUserCollisionException -> {
                            error?.value = "Такой пользователь уже существует!"
                            loading?.value = false
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            error?.value = "Пользователь не существует. Попробуйте ввести корректные данные!"
                            loading?.value = false
                        }
                        is FirebaseAuthException -> {
                            error?.value = "Попробуйте позже, сервер временно не отвечает"
                            loading?.value = false
                        }
                    }
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                error?.value = "Такой пользователь уже существует!"
                loading?.value = false
            } catch (e: FirebaseAuthException) {
                error?.value = "Попробуйте позже, сервер временно не отвечает"
                loading?.value = false
            }
        }
    }
}
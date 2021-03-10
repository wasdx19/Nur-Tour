package com.example.nurtour.domain.base

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException

abstract class FirestoreDocRefUseCase<in Params> {

    abstract fun run(
        params: Params,
        response: (Task<DocumentReference>) -> Unit
    )

    fun request(
        params: Params,
        onResult: () -> Unit,
        loading: MutableLiveData<Boolean>? = null,
        error: MutableLiveData<String>? = null
    ) {
        loading?.value = true

        run(params) {
            try {
                it.addOnSuccessListener {
                    onResult.invoke()
                    loading?.value = false
                }.addOnFailureListener {
                    when (it) {
                        is FirebaseNetworkException -> {
                            error?.value = "Проблемы с соединением. Проверьте интернет"
                            loading?.value = false
                        }
                        is FirebaseFirestoreException -> {
                            error?.value = "Попробуйте позже, сервер временно не отвечает"
                            loading?.value = false
                        }
                    }
                }
            } catch (e: FirebaseFirestoreException) {
                error?.value = "Попробуйте позже, сервер временно не отвечает"
                loading?.value = false
            }
        }
    }
}
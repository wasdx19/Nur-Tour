package com.example.nurtour.domain.base

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

abstract class FirestoreUseCase<in Params> {

    abstract fun run(
        params: Params,
        response: (Task<QuerySnapshot>) -> Unit
    )

    fun request(
        params: Params,
        onResult: (List<DocumentSnapshot?>) -> Unit,
        loading: MutableLiveData<Boolean>? = null,
        error: MutableLiveData<String>? = null
    ) {
        loading?.value = true

        run(params) {
            try {
                it.addOnSuccessListener {
                    onResult.invoke(it.documents)
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
package com.example.gengolearning.model.tokens

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseTokenProvider: TokenProvider {
    override suspend fun getToken(): String? =
        suspendCancellableCoroutine { cont->
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                cont.resume(null)
                return@suspendCancellableCoroutine
            }

            user.getIdToken(true)
                .addOnSuccessListener { result ->
                    cont.resume(result.token)
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }

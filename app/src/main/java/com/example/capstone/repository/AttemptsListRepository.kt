package com.example.capstone.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.model.Attempt
import com.example.capstone.model.AttemptsList
import com.example.capstone.model.Skater
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.sql.Timestamp

class AttemptsListRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var attemptDocument =
        firestore.collection("Attempts")

    private val _attemptsList: MutableLiveData<AttemptsList> = MutableLiveData()

    val attemptsList: LiveData<AttemptsList>
        get() = _attemptsList

    //the CreateAttemptFragment can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    suspend fun getAttemptsList() {
        Log.i("OK", "In the repo get attempt")

        val list = arrayListOf<Attempt>()

        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
//
                val dataAttempts = attemptDocument
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val id          = document.id.toInt()
                            var skaterId    = document.data["skaterId"].toString().toInt()
                            var clockedBy   = document.data["clockedBy"].toString().toInt()
                            val season      = document.data["season"].toString().toInt()
                            val time        = document.data["time"].toString()
                            val weather     = document.data["weather"].toString()
                            list.add(Attempt(id, skaterId, clockedBy,season, time, weather))
                        }
                    }
                    .await()
                _attemptsList.value = AttemptsList(list);
            }
        }  catch (e : Exception) {
            throw AttemptRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    class AttemptSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class AttemptRetrievalError(message: String) : Exception(message)
}

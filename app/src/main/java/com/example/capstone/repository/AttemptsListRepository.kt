package com.example.capstone.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.model.Attempt
import com.example.capstone.model.AttemptsList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.sql.Date
import java.text.SimpleDateFormat

class AttemptsListRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var attemptsCollection =
        firestore.collection("Attempts")

    private val _attemptsList: MutableLiveData<AttemptsList> = MutableLiveData()

    val attemptsList: LiveData<AttemptsList>
        get() = _attemptsList

    //the CreateAttemptFragment can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    private fun documentsToAttemptsList(documents: QuerySnapshot): ArrayList<Attempt> {
        val list = arrayListOf<Attempt>()
        for (document in documents) {
            val id          = document.id
            val skaterId    = document.data["skaterId"].toString().toInt()
            val clockedBy   = document.data["clockedBy"].toString().toInt()
            val season      = document.data["season"].toString().toInt()
            val time        = document.data["time"].toString()
            val weather     = document.data["weather"].toString()

            val dateInput = document.data["date"] as com.google.firebase.Timestamp

            list.add(Attempt(id, skaterId, clockedBy,season, time, weather, dateInput))
        }
        return list
    }

    suspend fun getAttemptsListAllSeasons() {
        val list = arrayListOf<Attempt>()
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                attemptsCollection
                    .orderBy("time")
                    .get()
                    .addOnSuccessListener { documents ->
                        list.addAll(documentsToAttemptsList(documents))
                    }
                    .await()
                _attemptsList.value = AttemptsList(list);
            }
        }  catch (e : Exception) {
            throw AttemptRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun getAttemptsList(season: Int) {
        val list = arrayListOf<Attempt>()
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                attemptsCollection
                        .orderBy("time")
                        .whereEqualTo("season", season)
                        .get()
                        .addOnSuccessListener { documents ->
                            list.addAll(documentsToAttemptsList(documents))
                        }
                        .await()
                _attemptsList.value = AttemptsList(list);
            }
        }  catch (e : Exception) {
            throw AttemptRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun getAttemptsListSkater(skaterId: Int) {
        val list = arrayListOf<Attempt>()
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                attemptsCollection
                    .orderBy("time")
                    .whereEqualTo("skaterId", skaterId)
                    .get()
                    .addOnSuccessListener { documents ->
                        list.addAll(documentsToAttemptsList(documents))
                    }
                    .await()
                _attemptsList.value = AttemptsList(list);
            }
        }  catch (e : Exception) {
            throw AttemptRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun addAttempt(attempt: Attempt){
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                attemptsCollection
                        .document().set(attempt)
                        .await()
            }
        }  catch (e : Exception) {
            throw AttemptRetrievalError("Adding-firebase-task was unsuccessful")
        }
    }

    class AttemptSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class AttemptRetrievalError(message: String) : Exception(message)
}

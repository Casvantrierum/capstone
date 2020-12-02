package com.example.capstone.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.model.Attempt
import com.example.capstone.model.AttemptsList
import com.google.common.collect.Lists
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class AttemptsListRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var attemptsCollection =
        firestore.collection("Attempts").orderBy("time")

    private val _attemptsList: MutableLiveData<AttemptsList> = MutableLiveData()

    val attemptsList: LiveData<AttemptsList>
        get() = _attemptsList

    //the CreateAttemptFragment can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    private fun documentsToSkatersList(documents: QuerySnapshot): ArrayList<Attempt> {
        val list = arrayListOf<Attempt>()
        for (document in documents) {
            val id          = document.id.toInt()
            val skaterId    = document.data["skaterId"].toString().toInt()
            val clockedBy   = document.data["clockedBy"].toString().toInt()
            val season      = document.data["season"].toString().toInt()
            val time        = document.data["time"].toString()
            val weather     = document.data["weather"].toString()

            val dateInput = document.data["date"] as com.google.firebase.Timestamp
            val milliseconds = dateInput.seconds * 1000 + dateInput.nanoseconds / 1000000
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(milliseconds)
            val date = sdf.format(netDate).toString()

            list.add(Attempt(id, skaterId, clockedBy,season, time, weather, date))
            Log.i("ADD", "$skaterId : $date")
        }
        return list
    }

    suspend fun getAttemptsListAllSeasons() {
        val list = arrayListOf<Attempt>()
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                attemptsCollection
                    .get()
                    .addOnSuccessListener { documents ->
                        list.addAll(documentsToSkatersList(documents))
                    }
                    .await()
                _attemptsList.value = AttemptsList(list);
            }
        }  catch (e : Exception) {
            throw AttemptRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun getAttemptsList(season: Int) {
        var list = arrayListOf<Attempt>()
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                attemptsCollection
                        .whereEqualTo("season", season)
                        .get()
                        .addOnSuccessListener { documents ->
                            list.addAll(documentsToSkatersList(documents))
                        }
                        .await()
                _attemptsList.value = AttemptsList(list);
            }
        }  catch (e : Exception) {
            throw AttemptRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun getAttemptsListSkater(skaterId: Int) {
        var list = arrayListOf<Attempt>()
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                attemptsCollection
                    .whereEqualTo("skaterId", skaterId)
                    .get()
                    .addOnSuccessListener { documents ->
                        list.addAll(documentsToSkatersList(documents))
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

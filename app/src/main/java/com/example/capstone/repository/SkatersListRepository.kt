package com.example.capstone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.model.Skater
import com.example.capstone.model.SkatersList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout


class SkatersListRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var skatersCollection =
            firestore.collection("Skaters")

    private val _skatersList: MutableLiveData<SkatersList> = MutableLiveData()

    val skatersList: LiveData<SkatersList>
        get() = _skatersList


    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    suspend fun getSkatersList() {

        val list = arrayListOf<Skater>()

        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
//
                skatersCollection
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val id      = document.id.toInt()
                            var name    = document.data["name"].toString()
                            var sex     = document.data["sex"].toString()
                            val ssrId   = document.data["ssrId"].toString().toInt()
                            list.add(Skater(id, name, sex, ssrId))
                        }
                    }
                    .await()
                _skatersList.value = SkatersList(list);
            }
        }  catch (e : Exception) {
            throw SkatersListRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    class SkatersListSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class SkatersListRetrievalError(message: String) : Exception(message)
}

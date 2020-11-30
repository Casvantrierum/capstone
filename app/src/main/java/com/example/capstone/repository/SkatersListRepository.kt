package com.example.capstone.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.model.Attempt
import com.example.capstone.model.Skater
import com.example.capstone.model.SkatersList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout


class SkatersListRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var skatersMaleCollection =
            firestore.collection("Skaters").whereEqualTo("sex", "m")
    private var skatersFemaleCollection =
            firestore.collection("Skaters").whereEqualTo("sex", "f")

    private val _maleSkatersList: MutableLiveData<SkatersList> = MutableLiveData()
    val maleSkatersList: LiveData<SkatersList>
        get() = _maleSkatersList

    private val _femaleSkatersList: MutableLiveData<SkatersList> = MutableLiveData()
    val femaleSkatersList: LiveData<SkatersList>
        get() = _femaleSkatersList

    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    private fun docsToSkatersList(documents: QuerySnapshot): ArrayList<Skater> {
        val list = arrayListOf<Skater>()
        for (document in documents) {
            val id      = document.id.toInt()
            var name    = document.data["name"].toString()
            var sex     = document.data["sex"].toString()
            val ssrId   = document.data["ssrId"].toString().toInt()
            list.add(Skater(id, name, sex, ssrId))
        }
        Log.i("ok", "list: $list")
        return list
    }

    suspend fun getSkatersList() {
        val listMale = arrayListOf<Skater>()
        val listFemale = arrayListOf<Skater>()
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(20_000) {
                skatersFemaleCollection
                        .get()
                        .addOnSuccessListener { documents ->
                            listFemale.addAll(docsToSkatersList(documents))
                        }
                        .await()
                _femaleSkatersList.value = SkatersList(listFemale)
                skatersMaleCollection
                        .get()
                        .addOnSuccessListener { documents ->
                            listMale.addAll(docsToSkatersList(documents))
                        }
                        .await()
                _maleSkatersList.value = SkatersList(listMale)
            }
        }  catch (e : Exception) {
            throw SkatersListRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    class SkatersListSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class SkatersListRetrievalError(message: String) : Exception(message)
}

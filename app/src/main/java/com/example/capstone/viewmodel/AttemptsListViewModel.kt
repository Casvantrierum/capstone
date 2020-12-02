package com.example.capstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.model.Attempt
import com.example.capstone.model.AttemptsList
import com.example.capstone.model.Skater
import com.example.capstone.model.SkatersList
import com.example.capstone.repository.AttemptsListRepository
import kotlinx.coroutines.launch
import kotlin.collections.arrayListOf as arrayListOf

class AttemptsListViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"
    private val attemptsListRepository: AttemptsListRepository = AttemptsListRepository()

    val attemptsList: LiveData<AttemptsList> = attemptsListRepository.attemptsList

    private val _attemptsListFiltered: MutableLiveData<AttemptsList> = MutableLiveData()
    val attemptsListFiltered: LiveData<AttemptsList>
        get() = _attemptsListFiltered

    val createSuccess: LiveData<Boolean> = attemptsListRepository.createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getAttemptsList(season: Int?) {
        viewModelScope.launch {
            try {
                if (season != null){
                    Log.i("JA", "naar getAttemptsList want season = $season")
                    attemptsListRepository.getAttemptsList(season)
                }
                else {
                    Log.i("JA", "naar getAttemptsListAllSeasons want season = $season")
                    attemptsListRepository.getAttemptsListAllSeasons()
                }

            } catch (ex: AttemptsListRepository.AttemptRetrievalError) {
                val errorMsg = "Something went wrong while retrieving attempt"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun getAttemptsListSkater(skaterId: Int) {
        viewModelScope.launch {
            try {
                attemptsListRepository.getAttemptsListSkater(skaterId)
            } catch (ex: AttemptsListRepository.AttemptRetrievalError) {
                val errorMsg = "Something went wrong while retrieving attempt"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun getAttemptListFiltered(skatersList: List<Skater>){
        val filteredList: ArrayList<Attempt> = arrayListOf()
        if (skatersList.isEmpty()
                || attemptsList.value == null
                || attemptsList.value!!.attemptsList.isEmpty())
            _attemptsListFiltered.value = AttemptsList(filteredList)
        else {
            for(attempt in attemptsListRepository.attemptsList.value?.attemptsList!!){
                val skatersFound : List<Skater> = skatersList.filter{ s -> s.id == attempt.skaterId}
                val fasterAttemptsSameSkaterFound : List<Attempt> = attemptsListRepository.attemptsList.value?.attemptsList!!.filter{ a -> a.skaterId == attempt.skaterId && a.time < attempt.time}
                if(skatersFound.size == 1  && fasterAttemptsSameSkaterFound.isEmpty()) {
                    filteredList.add(attempt)
                }
            }
            _attemptsListFiltered.value = AttemptsList(filteredList)
        }
    }
}
package com.example.capstone.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.model.attempts.Attempt
import com.example.capstone.model.attempts.AttemptsList
import com.example.capstone.model.skaters.Skater
import com.example.capstone.repository.AttemptsListRepository
import kotlinx.coroutines.launch
import kotlin.collections.arrayListOf as arrayListOf

class AttemptsListViewModel(application: Application) : AndroidViewModel(application) {
    private val attemptsListRepository: AttemptsListRepository = AttemptsListRepository()

    val attemptsList: LiveData<AttemptsList> = attemptsListRepository.attemptsList

    val attemptsListOfSkater: LiveData<AttemptsList> = attemptsListRepository.attemptsListOfSkater

    private val _attemptsListFiltered: MutableLiveData<AttemptsList> = MutableLiveData()
    val attemptsListFiltered: LiveData<AttemptsList>
        get() = _attemptsListFiltered

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getAttemptsList(season: Int?) {
        viewModelScope.launch {
            try {
                if (season != null){
                    attemptsListRepository.getAttemptsList(season)
                }
                else {
                    attemptsListRepository.getAttemptsListAllSeasons()
                }
            } catch (ex: AttemptsListRepository.AttemptRetrievalError) {
                _errorText.value = ex.message
            }
        }
    }

    fun getAttemptsListSkater(skaterId: Int) {
        viewModelScope.launch {
            try {
                attemptsListRepository.getAttemptsListSkater(skaterId)
            } catch (ex: AttemptsListRepository.AttemptRetrievalError) {
                _errorText.value = ex.message
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
            for(attempt in attemptsList.value?.attemptsList!!){
                val skatersFound : List<Skater> = skatersList.filter{ s -> s.id == attempt.skaterId}
                val fasterAttemptsSameSkaterFound : List<Attempt> = attemptsListRepository.attemptsList.value?.attemptsList!!.filter{ a -> a.skaterId == attempt.skaterId && a.time < attempt.time}
                if(skatersFound.size == 1  && fasterAttemptsSameSkaterFound.isEmpty()) {
                    filteredList.add(attempt)
                }
            }
            _attemptsListFiltered.value = AttemptsList(filteredList)
        }
    }

    fun getRankingPerYear(skaterId: Int, skatersListSameSex: List<Skater>): ArrayList<Array<Int>> {

        val skatersAttemptsList : List<Attempt> = attemptsList.value?.attemptsList!!.filter{ a -> a.skaterId == skaterId}

        val latestYear = skatersAttemptsList.maxByOrNull { it.season }?.season
        val firstYear = skatersAttemptsList.minByOrNull { it.season }?.season

        //filter same sex ids
        val sameSexIds: ArrayList<Int> = arrayListOf()
        for(skater in skatersListSameSex){
            sameSexIds.add(skater.id)
        }

        val rankings: ArrayList<Array<Int>> = arrayListOf()

        if (latestYear != null && firstYear != null) {
            for (season in firstYear..latestYear) {
                val skatersFastestAttempt: Attempt? = attemptsList.value?.attemptsList!!
                        .filter{ a -> a.skaterId == skaterId && a.season == season}
                        .minByOrNull { it.time }

                if (skatersFastestAttempt == null) rankings.add(arrayOf(season, 0))
                else {
                    val fasterAttempts: List<Attempt> = attemptsList.value?.attemptsList!!
                            .filter { a ->
                                a.skaterId != skaterId
                                        && a.time < skatersFastestAttempt.time
                                        && a.season == season
                                        && a.skaterId in sameSexIds
                            }
                    val amountFasterSkaters: Int = fasterAttempts.distinctBy { it.skaterId }.size

                    rankings.add(arrayOf(season, amountFasterSkaters + 1))
                }
            }
        }
        return rankings
    }
}
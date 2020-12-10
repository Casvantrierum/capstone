package com.example.capstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.model.Skater
import com.example.capstone.model.SkatersList
import com.example.capstone.repository.SkatersListRepository
import kotlinx.coroutines.launch

class SkatersListViewModel(application: Application) : AndroidViewModel(application)  {
    private val TAG = "FIRESTORE"
    private val skatersListRepository: SkatersListRepository = SkatersListRepository()

    val allSkatersList: LiveData<SkatersList> = skatersListRepository.allSkatersList
    val maleSkatersList: LiveData<SkatersList> = skatersListRepository.maleSkatersList
    val femaleSkatersList: LiveData<SkatersList> = skatersListRepository.femaleSkatersList

    val createSuccess: LiveData<Boolean> = skatersListRepository.createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getSkatersList() {
        viewModelScope.launch {
            try {
                skatersListRepository.getSkatersList()
            } catch (ex: SkatersListRepository.SkatersListRetrievalError) {
                val errorMsg = "Something went wrong while retrieving skaters list"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }
}
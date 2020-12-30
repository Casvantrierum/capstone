package com.example.capstone.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.model.skaters.SkatersList
import com.example.capstone.repository.SkatersListRepository
import kotlinx.coroutines.launch

class SkatersListViewModel(application: Application) : AndroidViewModel(application)  {
    private val skatersListRepository: SkatersListRepository = SkatersListRepository()

    val allSkatersList: LiveData<SkatersList> = skatersListRepository.allSkatersList
    val maleSkatersList: LiveData<SkatersList> = skatersListRepository.maleSkatersList
    val femaleSkatersList: LiveData<SkatersList> = skatersListRepository.femaleSkatersList

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getSkatersList() {
        viewModelScope.launch {
            try {
                skatersListRepository.getSkatersList()
            } catch (ex: SkatersListRepository.SkatersListRetrievalError) {
                val errorMsg = "Something went wrong while retrieving skaters list"
                _errorText.value = errorMsg
            }
        }
    }
}
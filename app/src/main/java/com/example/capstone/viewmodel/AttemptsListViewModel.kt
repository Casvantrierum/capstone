package com.example.capstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.model.AttemptsList
import com.example.capstone.repository.AttemptsListRepository
import kotlinx.coroutines.launch

class AttemptsListViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"
    private val attemptsListRepository: AttemptsListRepository = AttemptsListRepository()

    val attemptsList: LiveData<AttemptsList> = attemptsListRepository.attemptsList

    val createSuccess: LiveData<Boolean> = attemptsListRepository.createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getAttemptsList() {
        viewModelScope.launch {
            try {
                attemptsListRepository.getAttemptsList()
                Log.i("OK", "back in the vm get attempt")
            } catch (ex: AttemptsListRepository.AttemptRetrievalError) {
                val errorMsg = "Something went wrong while retrieving attempt"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }
}
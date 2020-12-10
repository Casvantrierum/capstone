package com.example.capstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.model.SSRResult
import com.example.capstone.model.Skater
import com.example.capstone.repository.SSRRepository
import kotlinx.coroutines.launch

class SSRViewModel(application: Application) : AndroidViewModel(application) {

    private val ssrRepository: SSRRepository = SSRRepository()

    val results: LiveData<SSRResult> = ssrRepository.resultPersonalRecord

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getSSRPersonalRecord(skater: Skater) {
        if (skater.ssrId != null){
            viewModelScope.launch {
                try {
                    //the MovieListRepository sets it's own livedata property
                    //our own MovieList LiveData property points to te one in that repository
                    ssrRepository.getSSRPersonalRecord(skater.ssrId)
                } catch (error: SSRRepository.SSRRefreshError) {
                    _errorText.value = error.message
                    Log.e("SSR error", error.cause.toString())
                }
            }
        }
    }
}
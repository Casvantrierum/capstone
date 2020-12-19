package com.example.capstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.model.Skater
import com.example.capstone.repository.AttemptsListRepository
import kotlinx.coroutines.launch

class SkaterViewModel (application: Application) : AndroidViewModel(application)  {

    private val _skater: MutableLiveData<Skater> = MutableLiveData()
    val skater: LiveData<Skater>
        get() = _skater

    fun setSkater(selectedSkater: Skater) {
        _skater.value = selectedSkater
    }
}
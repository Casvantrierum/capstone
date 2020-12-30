package com.example.capstone.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.model.skaters.Skater

class SkaterViewModel (application: Application) : AndroidViewModel(application)  {

    private val _skater: MutableLiveData<Skater> = MutableLiveData()
    val skater: LiveData<Skater>
        get() = _skater

    fun setSkater(selectedSkater: Skater) {
        _skater.value = selectedSkater
    }
}
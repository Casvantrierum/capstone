package com.example.capstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.model.Skater

class SkaterViewModel (application: Application) : AndroidViewModel(application)  {

    private val _skater: MutableLiveData<Skater> = MutableLiveData()
    val skater: LiveData<Skater>
        get() = _skater


    fun setSkater(selectedSkater: Skater) {
        Log.i("IN","setSkater()")
        _skater.value = selectedSkater
    }

    fun getSSRResults() {
        Log.i("IN","getSSRResults()")
        if (skater.value?.ssrId != 0){
            Log.i("SSR", "it has an ssr id")
        }
    }
}
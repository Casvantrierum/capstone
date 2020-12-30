package com.example.capstone.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.model.Attempt
import com.example.capstone.model.Skater
import com.example.capstone.repository.AttemptsListRepository
import com.example.capstone.repository.SSRRepository
import com.example.capstone.repository.SkatersListRepository
import kotlinx.coroutines.launch
import java.util.*


class AddViewModel(application: Application) : AndroidViewModel(application)  {
    private val TAG = "FIRESTORE"
    private val skatersListRepository: SkatersListRepository = SkatersListRepository()
    private val attemptsListRepository: AttemptsListRepository = AttemptsListRepository()

    private val ssrRepository: SSRRepository = SSRRepository()

    private val _fetching: MutableLiveData<Boolean> = MutableLiveData()
    val fetching: LiveData<Boolean>
        get() = _fetching

    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText


    fun addSkater(new: Boolean, firstname: String, lastname: String, sex: String, skitsID: Int,
                  time: String, weather: String, day: String, month: String, year: String) {
        viewModelScope.launch {
            _fetching.value = true
            try {
                if (new) {
                    ssrRepository.getSSRId(firstname, lastname)
                    val ssrId = ssrRepository.resultId.value?.skaters?.get(0)?.id
                    skatersListRepository.addSkater(
                            Skater(
                                    skitsID, firstname, lastname, sex, ssrId
                            )
                    )
                    skatersListRepository.getSkatersList()
                }

                // the correct way to get today's date

                // the correct way to get today's date
                val calendar = Calendar.getInstance()
                calendar.set(year.toInt(), month.toInt() - 1, day.toInt(), 0, 0);
                val seconds: Long = calendar.timeInMillis / 1000

                val season: Int
                if (month.toInt() >= 6) season = year.toInt()
                else season = year.toInt() - 1

                val firebaseTimeStamp = com.google.firebase.Timestamp(seconds, 0)

                attemptsListRepository.addAttempt(
                        Attempt(
                                skitsID,
                                0,
                                season,
                                time,
                                weather,
                                firebaseTimeStamp
                        )
                )

                _createSuccess.value = true;

            } catch (ex: SkatersListRepository.SkatersListSaveError) {
                val errorMsg = "Something went wrong while adding a skater"
                Log.e(TAG, ex.message ?: errorMsg)
                Log.i("OEPS", errorMsg)
                _errorText.value = errorMsg
                _createSuccess.value = false;
            }
            catch (ex: AttemptsListRepository.AttemptSaveError) {
                val errorMsg = "Something went wrong while adding a n attempt"
                Log.e(TAG, ex.message ?: errorMsg)
                Log.i("OEPS", errorMsg)
                _errorText.value = errorMsg
                _createSuccess.value = false;
            }

            _fetching.value = false
        }
    }
}
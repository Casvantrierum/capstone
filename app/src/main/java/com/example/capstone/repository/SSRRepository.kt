package com.example.capstone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.api.SSRApi
import com.example.capstone.api.SSRApiService
import com.example.capstone.model.SSRResult
import kotlinx.coroutines.withTimeout

class SSRRepository {

    private val ssrApiService: SSRApiService = SSRApi.createApi()

    private val _result: MutableLiveData<SSRResult> = MutableLiveData()

    val result: LiveData<SSRResult>
        get() = _result

    suspend fun getSSRPersonalRecord(ssrId: Int) {
        try {
            //timeout the request after 5 seconds
            val result = withTimeout(5_000) {
                ssrApiService.getSSRPersonalRecord(ssrId)
            }
            _result.value = result
        } catch (error: Throwable) {
            throw SSRRefreshError("Unable to refresh speedskating results data", error)
        }
    }

    class SSRRefreshError(message: String, cause: Throwable) : Throwable(message, cause)
}
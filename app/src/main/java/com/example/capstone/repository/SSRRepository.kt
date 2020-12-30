package com.example.capstone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone.api.SSRApi
import com.example.capstone.api.SSRApiService
import com.example.capstone.model.ssrId.IDLookUpResult
import com.example.capstone.model.ssrPR.SSRResult
import kotlinx.coroutines.withTimeout

class SSRRepository {

    private val ssrApiService: SSRApiService = SSRApi.createApi()

    private val _resultPersonalRecord: MutableLiveData<SSRResult> = MutableLiveData()
    val resultPersonalRecord: LiveData<SSRResult>
        get() = _resultPersonalRecord

    private val _resultId: MutableLiveData<IDLookUpResult> = MutableLiveData()
    val resultId: LiveData<IDLookUpResult>
        get() = _resultId

    suspend fun getSSRPersonalRecord(ssrId: Int) {
        try {
            //timeout the request after 5 seconds
            val result = withTimeout(5_000) {
                ssrApiService.getSSRPersonalRecord(ssrId)
            }
            _resultPersonalRecord.value = result
        } catch (error: Throwable) {
            throw SSRRefreshError("Unable to refresh speedskating results data", error)
        }
    }

    suspend fun getSSRId(firstname: String, lastname: String) {
        try {
            //timeout the request after 5 seconds
            val result = withTimeout(5_000) {
                ssrApiService.getSSRId(firstname, lastname)
            }
            _resultId.value = result
        } catch (error: Throwable) {
            throw SSRRefreshError("Unable to refresh speedskating id data", error)
        }
    }

    class SSRRefreshError(message: String, cause: Throwable) : Throwable(message, cause)
}
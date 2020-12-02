package com.example.capstone.model

import com.google.gson.annotations.SerializedName

data class SSRResult (

    @SerializedName("skater")
    val skater : Int,

    @SerializedName("records")
    val records : ArrayList<SSRPersonalRecord>
)
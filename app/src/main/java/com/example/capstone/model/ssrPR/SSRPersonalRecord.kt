package com.example.capstone.model.ssrPR

import com.google.gson.annotations.SerializedName

data class SSRPersonalRecord (
    @SerializedName("distance")
    val distance : Int,

    @SerializedName("time")
    val time : String,

    @SerializedName("date")
    val date : String,

    @SerializedName("location")
    val location : String
)

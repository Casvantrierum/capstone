package com.example.capstone.model.ssrId

import com.google.gson.annotations.SerializedName

data class IDLookUpResult (
    @SerializedName("skaters")
    val skaters : List<Skaters>
)
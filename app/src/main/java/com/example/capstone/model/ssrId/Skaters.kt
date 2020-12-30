package com.example.capstone.model.ssrId

import com.google.gson.annotations.SerializedName

data class Skaters (

        @SerializedName("id")
        val id : Int,

        @SerializedName("familyname")
        val familyname : String,

        @SerializedName("givenname")
        val givenname : String,

        @SerializedName("country")
        val country : String,

        @SerializedName("gender")
        val gender : String,

        @SerializedName("category")
        val category : String

)
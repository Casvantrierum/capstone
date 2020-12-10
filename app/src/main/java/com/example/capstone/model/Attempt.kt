package com.example.capstone.model

import com.google.firebase.Timestamp

data class Attempt(
        val id: String,
        val skaterId: Int,
        val clockedBy: Int,
        val season: Int,
        val time: String,
        val weather: String,
        val date: Timestamp
    //TODO TO DO add time stamp
)
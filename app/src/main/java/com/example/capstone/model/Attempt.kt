package com.example.capstone.model

import com.google.firebase.Timestamp

data class Attempt(
        val skaterId: Int,
        val clockedBy: Int,
        val season: Int,
        val time: String,
        val weather: String,
        val date: Timestamp
)
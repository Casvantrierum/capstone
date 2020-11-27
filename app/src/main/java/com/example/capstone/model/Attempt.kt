package com.example.capstone.model

data class Attempt(
    val id: Int,
    val skaterId: Int,
    val clockedBy: Int,
    val season: Int,
    val time: String,
    val weather: String
    //TODO TO DO add time stamp
)
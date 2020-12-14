package com.example.capstone.model

data class Skater (
        val id: Int,
        val firstname: String,
        val lastname: String,
        val sex: String,
        val ssrId: Int?
) {
        override fun toString(): String = "$firstname $lastname"
}
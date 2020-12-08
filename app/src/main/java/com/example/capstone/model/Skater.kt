package com.example.capstone.model

data class Skater (
        val id: Int,
        val name: String,
        val sex: String,
        val ssrId: Int?
) {
        override fun toString(): String = name
}
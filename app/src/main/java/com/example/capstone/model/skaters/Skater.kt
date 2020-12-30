package com.example.capstone.model.skaters

import android.content.res.Resources
import com.example.capstone.R

data class Skater (
        val id: Int,
        val firstname: String,
        val lastname: String,
        val sex: String,
        val ssrId: Int? = null
) {
        override fun toString(): String = "$firstname $lastname"
}
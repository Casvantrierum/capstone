package com.example.capstone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.model.Attempt
import com.example.capstone.R
import com.example.capstone.model.Skater
import kotlinx.android.synthetic.main.item_standing.view.*

class StandingAdapter (private val attemptsList: List<Attempt>, private val skatersList: List<Skater>) :
    RecyclerView.Adapter<StandingAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_standing, parent, false)
        )
    }

    override fun getItemCount(): Int = attemptsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skatersFound : List<Skater> = skatersList.filter{ s -> (s.id == attemptsList[position].skaterId)}
        if(skatersFound.size == 1 ) {
            val skater = skatersFound.single()
            holder.databind(attemptsList[position], skater, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(attempt: Attempt, skater: Skater, position: Int) {
            val ranking = position+1
            itemView.tvRank.text = "$ranking."
            itemView.tvName.text = skater.name
            itemView.tvTime.text = attempt.time
        }
    }
}

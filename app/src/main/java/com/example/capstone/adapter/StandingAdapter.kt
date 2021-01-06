package com.example.capstone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.model.attempts.Attempt
import com.example.capstone.R
import com.example.capstone.model.skaters.Skater
import kotlinx.android.synthetic.main.item_standing.view.*
import java.sql.Date
import java.text.SimpleDateFormat

class StandingAdapter (private val attemptsList: List<Attempt>, private val skatersList: List<Skater>, private val onClick: (Skater) -> Unit) :
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
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun databind(attempt: Attempt, skater: Skater, position: Int) {
            val ranking = position+1

            val dateInput = attempt.date
            val milliseconds = dateInput.seconds * 1000 + dateInput.nanoseconds / 1000000
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(milliseconds)
            val date = sdf.format(netDate).toString()

            itemView.tvYear.text = ranking.toString()
            itemView.tvName.text = "${skater.firstname} ${skater.lastname}"
            itemView.tvDistance.text = attempt.time
            itemView.tvTime.text = date
        }

        init {
            itemView.setOnClickListener {
                val skatersFound: Skater =
                    skatersList.single { s -> (s.id == attemptsList[adapterPosition].skaterId) }
                onClick(skatersFound)
            }
        }
    }
}

package com.example.capstone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.model.Attempt
import kotlinx.android.synthetic.main.item_result.view.*
import java.sql.Date
import java.text.SimpleDateFormat

class SkaterResultAdapter (private val attemptsList: List<Attempt>) :
        RecyclerView.Adapter<SkaterResultAdapter.ViewHolder>() {

        private lateinit var context: Context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            context = parent.context
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_result, parent, false)
            )
        }

        override fun getItemCount(): Int = attemptsList.size

        override fun onBindViewHolder(holder: SkaterResultAdapter.ViewHolder, position: Int) {
            holder.databind(attemptsList[position], position)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun databind(attempt: Attempt, position: Int) {
                val dateInput = attempt.date
                val milliseconds = dateInput.seconds * 1000 + dateInput.nanoseconds / 1000000
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val netDate = Date(milliseconds)
                val date = sdf.format(netDate).toString()

                itemView.tvDistance.text = attempt.time
                itemView.tvTime.text = date
                itemView.tvLocation.text = "Weather: ${attempt.weather}"
            }
        }
}
package com.example.capstone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.model.Attempt
import kotlinx.android.synthetic.main.item_result.view.*

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
                itemView.tvDistance.text = attempt.time
                itemView.tvTime.text = attempt.date
                itemView.tvLocation.text = "Weather: ${attempt.weather}"
            }
        }
}
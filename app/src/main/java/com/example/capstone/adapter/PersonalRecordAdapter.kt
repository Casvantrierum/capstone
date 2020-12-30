package com.example.capstone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.model.ssrPR.SSRPersonalRecord
import kotlinx.android.synthetic.main.item_ssr_result.view.*

class PersonalRecordAdapter (private val personalRecords: List<SSRPersonalRecord>) :
    RecyclerView.Adapter<PersonalRecordAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_ssr_result, parent, false)
        )
    }

    override fun getItemCount(): Int = personalRecords.size

    override fun onBindViewHolder(holder: PersonalRecordAdapter.ViewHolder, position: Int) {
        holder.databind(personalRecords[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(pr: SSRPersonalRecord) {
            itemView.tvDistance.text = pr.distance.toString()
            itemView.tvTime.text = pr.time
            itemView.tvDate.text = pr.date
            itemView.tvLocation.text = pr.location
        }
    }
}
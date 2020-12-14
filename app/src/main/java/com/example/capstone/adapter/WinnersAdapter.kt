package com.example.capstone.adapter

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.model.Attempt
import com.example.capstone.R
import com.example.capstone.model.Skater
import kotlinx.android.synthetic.main.item_standing.view.*
import kotlinx.android.synthetic.main.item_standing.view.tvYear
import kotlinx.android.synthetic.main.item_winners.view.*
import java.sql.Date
import java.text.SimpleDateFormat

class WinnersAdapter (
    private val maleSkaterWinnersList: List<Skater?>,
    private val maleAttemptWinnersList: List<Attempt?>,
    private val femaleSkaterWinnersList: List<Skater?>,
    private val femaleAttemptWinnersList: List<Attempt?>,
    private val currentSeason: Int,
    private val onClick: (Skater) -> Unit) :
    RecyclerView.Adapter<WinnersAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_winners, parent, false)
        )
    }

    override fun getItemCount(): Int = maleSkaterWinnersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(
            maleSkaterWinnersList[position],
            maleAttemptWinnersList[position],
            femaleSkaterWinnersList[position],
            femaleAttemptWinnersList[position]
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(skaterMale: Skater?, attemptMale: Attempt?, skaterFemale: Skater?, attemptFemale: Attempt?) {

            val season = currentSeason-adapterPosition

            itemView.tvYear.text = "$season"

            if (attemptMale != null){
                itemView.tvMaleFirstname.text = "${skaterMale?.firstname}"
                itemView.tvMaleLastname.text = "${skaterMale?.lastname}"
                itemView.tvMaleTime.text = attemptMale.time
            }
            else {
                itemView.tvMaleFirstname.text = ""
                itemView.tvMaleLastname.text = "No attempts"//todo hc
                itemView.tvMaleTime.text = ""
            }

            if (attemptFemale != null){
                itemView.tvFemaleFirstname.text = "${skaterFemale?.firstname}"
                itemView.tvFemaleLastname.text = "${skaterFemale?.lastname}"
                itemView.tvFemaleTime.text = attemptFemale.time
            }
            else {
                itemView.tvFemaleFirstname.text = ""
                itemView.tvFemaleLastname.text = "No attempts"//todo hc
                itemView.tvFemaleTime.text = ""
            }
        }

        init {
            itemView.tvMaleFirstname.setOnClickListener {
                maleSkaterWinnersList[adapterPosition]?.let { it1 -> onClick(it1) }
            }
            itemView.tvMaleLastname.setOnClickListener {
                maleSkaterWinnersList[adapterPosition]?.let { it1 -> onClick(it1) }
            }
            itemView.tvFemaleFirstname.setOnClickListener {
                femaleSkaterWinnersList[adapterPosition]?.let { it1 -> onClick(it1) }
            }
            itemView.tvFemaleLastname.setOnClickListener {
                femaleSkaterWinnersList[adapterPosition]?.let { it1 -> onClick(it1) }
            }
        }
    }
}

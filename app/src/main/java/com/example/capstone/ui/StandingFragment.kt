package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.adapter.StandingAdapter
import com.example.capstone.model.Attempt
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.AttemptsListViewModel
import com.example.capstone.viewmodel.SkaterViewModel
import com.example.capstone.viewmodel.SkatersListViewModel
import kotlinx.android.synthetic.main.fragment_standing.*


class StandingFragment : Fragment() {

    private val attemptsListViewModel: AttemptsListViewModel by viewModels()
    private val skatersListViewModel: SkatersListViewModel by activityViewModels()
    private val skaterViewModel: SkaterViewModel by activityViewModels()

    private var attemptsList = arrayListOf<Attempt>()
    private var attemptsListFiltered = arrayListOf<Attempt>()

    private var skatersList = arrayListOf<Skater>()
    private var maleSkatersList = arrayListOf<Skater>()
    private var femaleSkatersList = arrayListOf<Skater>()

    private var season: Int = 2020
    private var currentSeason: Int = 2020 //TODO get with date
    private var sex: String = "m"

    private lateinit var standingAdapter: StandingAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_standing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attemptsListViewModel.getAttemptsList(season)
        tvSeason.text = "Season $season-${season+1}"

        standingAdapter = StandingAdapter(attemptsListFiltered, skatersList, ::onSkaterClick)
        rvStanding.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvStanding.adapter = standingAdapter

        rgSex.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            attemptsListViewModel.getAttemptListFiltered(skatersList)
            standingAdapter.notifyDataSetChanged()

            skatersList.clear()
            if (radio.text == "Male"){//TODO HC
                sex = "m"
                skatersList.addAll(maleSkatersList)
            }
            else {
                sex = "f"
                skatersList.addAll(femaleSkatersList)
            }
            attemptsListViewModel.getAttemptListFiltered(skatersList)
        }

        view.findViewById<ImageButton>(R.id.ibUp).setOnClickListener {
            season++

            if(season > currentSeason + 1) season--
            else if(season == currentSeason + 1){
                attemptsListViewModel.getAttemptsList(null)
                tvSeason.text = "All time" //TODO hc
            }
            else {
                attemptsListViewModel.getAttemptsList(season)
                tvSeason.text = "Season $season-${season + 1}" //TODO hc
            }
        }
        view.findViewById<ImageButton>(R.id.ibDown).setOnClickListener {
            season--
            tvSeason.text = "Season $season-${season+1}" //TODO hc
            attemptsListViewModel.getAttemptsList(season)
        }

        attemptsListViewModel.attemptsList.observe(viewLifecycleOwner, {
            attemptsList.clear()
            attemptsList.addAll(it.attemptsList)

            attemptsListViewModel.getAttemptListFiltered(skatersList)
            standingAdapter.notifyDataSetChanged()
        })

        attemptsListViewModel.attemptsListFiltered.observe(viewLifecycleOwner, {
            attemptsListFiltered.clear()
            attemptsListFiltered.addAll(it.attemptsList)

            standingAdapter.notifyDataSetChanged()
        })

        skatersListViewModel.maleSkatersList.observe(viewLifecycleOwner, {
            maleSkatersList.clear()
            maleSkatersList.addAll(it.skatersList)

            if (sex == "m"){
                skatersList.clear()
                skatersList.addAll(maleSkatersList)
            }

            attemptsListViewModel.getAttemptListFiltered(skatersList)
        })

        skatersListViewModel.femaleSkatersList.observe(viewLifecycleOwner, {
            femaleSkatersList.clear()
            femaleSkatersList.addAll(it.skatersList)

            attemptsListViewModel.getAttemptListFiltered(skatersList)

            if (sex == "f"){
                skatersList.clear()
                skatersList.addAll(femaleSkatersList)
            }
            attemptsListViewModel.getAttemptListFiltered(skatersList)
        })
    }


    private fun onSkaterClick(skater: Skater) {
        //Log.i("CLICK", "${attempt.time}")
        Log.i("CLICK", "${skater.name}")
        skaterViewModel.setSkater(skater)
        findNavController().navigate(R.id.action_navigation_standing_to_skaterFragment)
    }
}
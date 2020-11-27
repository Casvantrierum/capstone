package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.adapter.StandingAdapter
import com.example.capstone.model.Attempt
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.AttemptsListViewModel
import com.example.capstone.viewmodel.SkatersListViewModel
import kotlinx.android.synthetic.main.fragment_standing.*


class StandingFragment : Fragment() {

    private val attemptsViewModel: AttemptsListViewModel by activityViewModels()
    private val skatersViewModel: SkatersListViewModel by activityViewModels()

    private var attemptsList = arrayListOf<Attempt>()
    private var skatersList = arrayListOf<Skater>()

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

        standingAdapter = StandingAdapter(attemptsList, skatersList)
        rvStanding.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvStanding.adapter = standingAdapter

        attemptsViewModel.attemptsList.observe(viewLifecycleOwner, {
            attemptsList.clear()
            attemptsList.addAll(it.attemptsList)
            Log.i("OBSERVE A", "dashboard fragment: $attemptsList" )

            standingAdapter.notifyDataSetChanged()
        })

        skatersViewModel.skatersList.observe(viewLifecycleOwner, {
            skatersList.clear()
            skatersList.addAll(it.skatersList)
            Log.i("OBSERVE S", "dashboard fragment: $skatersList" )

            standingAdapter.notifyDataSetChanged()
        })
    }
}
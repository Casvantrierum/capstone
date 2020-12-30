package com.example.capstone.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.adapter.WinnersAdapter
import com.example.capstone.model.attempts.Attempt
import com.example.capstone.model.skaters.Skater
import com.example.capstone.viewmodel.AttemptsListViewModel
import com.example.capstone.viewmodel.SkaterViewModel
import com.example.capstone.viewmodel.SkatersListViewModel
import kotlinx.android.synthetic.main.fragment_winners.*
import java.time.LocalDateTime

class WinnersFragment : Fragment() {

    private val attemptsListViewModel: AttemptsListViewModel by viewModels()
    private val skatersListViewModel: SkatersListViewModel by activityViewModels()
    private val skaterViewModel: SkaterViewModel by activityViewModels()

    private var attemptsList = arrayListOf<Attempt>()
    private var skatersList = arrayListOf<Skater>()
    private var maleSkatersList = arrayListOf<Skater>()
    private var femaleSkatersList = arrayListOf<Skater>()

    private var maleSkaterWinnersList = arrayListOf<Skater?>()
    private var maleAttemptWinnersList = arrayListOf<Attempt?>()
    private var femaleSkaterWinnersList = arrayListOf<Skater?>()
    private var femaleAttemptWinnersList = arrayListOf<Attempt?>()

    private var lastCompleteSeason: Int = 0

    private lateinit var winnersAdapter: WinnersAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set current season
        val current = LocalDateTime.now()
        val month = current.monthValue.toString().toInt()
        val year = current.year.toString().toInt() -1 //-1 beacuse currrent season is ongoing

        lastCompleteSeason = if (month >= 6) year
        else year - 1
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_winners, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attemptsListViewModel.getAttemptsList(null)

        winnersAdapter = WinnersAdapter(
            maleSkaterWinnersList,
            maleAttemptWinnersList,
            femaleSkaterWinnersList,
            femaleAttemptWinnersList,
            lastCompleteSeason,
            ::onSkaterClick
        )
        rvWinners.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvWinners.adapter = winnersAdapter

        attemptsListViewModel.attemptsList.observe(viewLifecycleOwner, {
            attemptsList.clear()
            attemptsList.addAll(it.attemptsList)
            formatData()
        })

        skatersListViewModel.allSkatersList.observe(viewLifecycleOwner, {
            skatersList.clear()
            skatersList.addAll(it.skatersList)

            formatData()
        })

        skatersListViewModel.maleSkatersList.observe(viewLifecycleOwner, {
            maleSkatersList.clear()
            maleSkatersList.addAll(it.skatersList)
        })

        skatersListViewModel.femaleSkatersList.observe(viewLifecycleOwner, {
            femaleSkatersList.clear()
            femaleSkatersList.addAll(it.skatersList)
        })

        skatersListViewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(
                    requireActivity(), it,
                    Toast.LENGTH_SHORT
            ).show()
        })

        attemptsListViewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(
                    requireActivity(), it,
                    Toast.LENGTH_SHORT
            ).show()
        })
    }

    private fun formatData(){

        maleSkaterWinnersList.clear()
        maleAttemptWinnersList.clear()
        femaleSkaterWinnersList.clear()
        femaleAttemptWinnersList.clear()

        val maleIds = arrayListOf<Int>()
        for (male in maleSkatersList) maleIds.add(male.id)

        val femaleIds = arrayListOf<Int>()
        for (female in femaleSkatersList) femaleIds.add(female.id)

        for (i in lastCompleteSeason downTo 1985 step 1) {
            val attemptMale = attemptsList.filter{ it.season == i && it.skaterId in maleIds}.minByOrNull {it.time}
            if (attemptMale != null){
                val skaterMale = maleSkatersList.filter{it.id == attemptMale.skaterId}
                maleSkaterWinnersList.add(skaterMale.single())
            }
            else maleSkaterWinnersList.add(null)
            maleAttemptWinnersList.add(attemptMale)

            val attemptFemale = attemptsList.filter{ it.season == i && it.skaterId in femaleIds}.minByOrNull {it.time}
            if (attemptFemale != null){
                val skaterFemale = femaleSkatersList.filter{it.id == attemptFemale.skaterId}
                femaleSkaterWinnersList.add(skaterFemale.single())
            }
            else femaleSkaterWinnersList.add(null)
            femaleAttemptWinnersList.add(attemptFemale)
        }

        winnersAdapter.notifyDataSetChanged()
    }


    private fun onSkaterClick(skater: Skater) {
        skaterViewModel.setSkater(skater)
        findNavController().navigate(R.id.action_navigation_winners_to_skaterFragment)
    }
}

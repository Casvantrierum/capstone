package com.example.capstone.ui

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.adapter.StandingAdapter
import com.example.capstone.model.attempts.Attempt
import com.example.capstone.model.skaters.Skater
import com.example.capstone.viewmodel.AttemptsListViewModel
import com.example.capstone.viewmodel.SkaterViewModel
import com.example.capstone.viewmodel.SkatersListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_standing.*
import java.time.LocalDateTime
import java.util.*


class StandingFragment : Fragment() {

    private val attemptsListViewModel: AttemptsListViewModel by viewModels()
    private val skatersListViewModel: SkatersListViewModel by activityViewModels()
    private val skaterViewModel: SkaterViewModel by activityViewModels()

    private var attemptsList = arrayListOf<Attempt>()
    private var attemptsListFiltered = arrayListOf<Attempt>()

    private var skatersList = arrayListOf<Skater>()
    private var maleSkatersList = arrayListOf<Skater>()
    private var femaleSkatersList = arrayListOf<Skater>()

    private var season: Int = 0
    private var currentSeason: Int = 0
    private lateinit var sex: String

    private  var user: FirebaseUser? = null

    private lateinit var standingAdapter: StandingAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set current season
        val current = LocalDateTime.now()
        val month = current.monthValue.toString().toInt()
        val year = current.year.toString().toInt()

        currentSeason = if (month >= 6) year
        else year - 1
        season = currentSeason
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_standing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pbStanding.visibility = View.VISIBLE

        sex = context?.getString(R.string.male_short).toString()

        user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            fab.show()
        }
        else fab.hide()

        if (season == currentSeason + 1){
            tvSeason.text = getString(R.string.all_time)
            attemptsListViewModel.getAttemptsList(null)
            ibUp.visibility = View.INVISIBLE
        }
        else{
            attemptsListViewModel.getAttemptsList(season)
            tvSeason.text = context?.getString(R.string.standing_season, season, season + 1)
            ibUp.visibility = View.VISIBLE
        }

        standingAdapter = StandingAdapter(attemptsListFiltered, skatersList, ::onSkaterClick)
        rvStanding.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvStanding.adapter = standingAdapter

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_standing_to_addAttemptFragment)
        }

        btnRetry.setOnClickListener {

            skatersListViewModel.getSkatersList()
            attemptsListViewModel.getAttemptsList(season)
        }

        rgSex.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            attemptsListViewModel.getAttemptListFiltered(skatersList)
            standingAdapter.notifyDataSetChanged()

            skatersList.clear()
            if (radio.text == getString(R.string.male)){
                sex = getString(R.string.male_short)
                skatersList.addAll(maleSkatersList)
            }
            else {
                sex = getString(R.string.female_short)
                skatersList.addAll(femaleSkatersList)
            }
            attemptsListViewModel.getAttemptListFiltered(skatersList)
        }

        view.findViewById<ImageButton>(R.id.ibUp).setOnClickListener {
            season++
            when {
                season > currentSeason + 1 -> season--
                season == currentSeason + 1 -> {
                    attemptsListViewModel.getAttemptsList(null)
                    tvSeason.text = getString(R.string.all_time)
                    ibUp.visibility = View.INVISIBLE
                }
                else -> {
                    attemptsListViewModel.getAttemptsList(season)
                    tvSeason.text = getString(R.string.standing_season, season, season + 1)
                }
            }
        }

        view.findViewById<ImageButton>(R.id.ibDown).setOnClickListener {
            season--
            tvSeason.text = getString(R.string.standing_season, season, season + 1)
            attemptsListViewModel.getAttemptsList(season)
            ibUp.visibility = View.VISIBLE
        }

        attemptsListViewModel.attemptsList.observe(viewLifecycleOwner, {

            skatersListViewModel.getSkatersList()

            attemptsList.clear()
            attemptsList.addAll(it.attemptsList)

            attemptsListViewModel.getAttemptListFiltered(skatersList)
            standingAdapter.notifyDataSetChanged()

            pbStanding.visibility = View.INVISIBLE
        })

        attemptsListViewModel.attemptsListFiltered.observe(viewLifecycleOwner, {
            attemptsListFiltered.clear()
            attemptsListFiltered.addAll(it.attemptsList)

            if (attemptsListFiltered.isEmpty()) {
                tvNoAttempts.visibility = View.VISIBLE
                var sexFull = getString(R.string.female).toLowerCase(Locale.ROOT)
                if (sex == getString(R.string.male_short))  sexFull = getString(R.string.male).toLowerCase(Locale.ROOT)
                tvNoAttempts.text = getString(R.string.no_attempts_this_year, sexFull)
            } else tvNoAttempts.visibility = View.INVISIBLE

            standingAdapter.notifyDataSetChanged()
        })


        skatersListViewModel.allSkatersList.observe(viewLifecycleOwner, {
            if (it.skatersList.size == 0){
                tvNoSkaters.visibility = View.VISIBLE
                btnRetry.visibility = View.VISIBLE
            }
            else {
                tvNoSkaters.visibility = View.INVISIBLE
                btnRetry.visibility = View.INVISIBLE
            }
        })


        skatersListViewModel.maleSkatersList.observe(viewLifecycleOwner, {
            maleSkatersList.clear()
            maleSkatersList.addAll(it.skatersList)

            if (sex == getString(R.string.male_short)) {
                skatersList.clear()
                skatersList.addAll(maleSkatersList)
            }
        })

        skatersListViewModel.femaleSkatersList.observe(viewLifecycleOwner, {
            femaleSkatersList.clear()
            femaleSkatersList.addAll(it.skatersList)

            if (sex == getString(R.string.female_short)) {
                skatersList.clear()
                skatersList.addAll(femaleSkatersList)
            }
            attemptsListViewModel.getAttemptListFiltered(skatersList)
        })

        skatersListViewModel.errorText.observe(viewLifecycleOwner, {
            pbStanding.visibility = View.INVISIBLE
            Toast.makeText(
                    context, it,
                    Toast.LENGTH_SHORT
            ).show()
        })

        attemptsListViewModel.errorText.observe(viewLifecycleOwner, {
            pbStanding.visibility = View.INVISIBLE
            Toast.makeText(
                    context, it,
                    Toast.LENGTH_LONG
            ).show()
        })
    }

    private fun onSkaterClick(skater: Skater) {
        skaterViewModel.setSkater(skater)
        findNavController().navigate(R.id.action_navigation_standing_to_skaterFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_login -> {
            findNavController().navigate(R.id.action_navigation_standing_to_loginFragment)
            true
        }
        R.id.action_logout -> {
            findNavController().navigate(R.id.action_navigation_standing_to_wedcieMemberFragment)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        if (user!= null) inflater.inflate(R.menu.topbar_loged_in, menu)
        else inflater.inflate(R.menu.topbar_loged_out, menu)
    }
}

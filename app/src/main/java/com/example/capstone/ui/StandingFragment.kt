package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.RadioButton
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    private var user: FirebaseUser? = null

    private lateinit var standingAdapter: StandingAdapter

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

        user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            fab.show()
            // Name, email address, and profile photo Url
            //val name = user.displayName
            val email = user!!.email
            //val photoUrl: Uri? = user.photoUrl

            // Check if user's email is verified
            //val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = user!!.uid
        }
        else fab.hide()

        if (season == currentSeason + 1){
            tvSeason.text = "All time" //TODO hc
            attemptsListViewModel.getAttemptsList(null)
            ibUp.visibility = View.INVISIBLE;
        }
        else{
            attemptsListViewModel.getAttemptsList(season)
            tvSeason.text = "Season $season-${season+1}"
            ibUp.visibility = View.VISIBLE;
        }

        standingAdapter = StandingAdapter(attemptsListFiltered, skatersList, ::onSkaterClick)
        rvStanding.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvStanding.adapter = standingAdapter


        fab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_standing_to_addAttemptFragment)
        }

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
                ibUp.visibility = View.INVISIBLE;
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
            ibUp.visibility = View.VISIBLE;
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

            if (sex == "m") {
                skatersList.clear()
                skatersList.addAll(maleSkatersList)
            }

            attemptsListViewModel.getAttemptListFiltered(skatersList)
        })

        skatersListViewModel.femaleSkatersList.observe(viewLifecycleOwner, {
            femaleSkatersList.clear()
            femaleSkatersList.addAll(it.skatersList)

            attemptsListViewModel.getAttemptListFiltered(skatersList)

            if (sex == "f") {
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

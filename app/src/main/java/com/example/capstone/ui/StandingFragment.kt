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

    private val attemptsListViewModel: AttemptsListViewModel by viewModels()
    private val skatersListViewModel: SkatersListViewModel by activityViewModels()

    private var attemptsList = arrayListOf<Attempt>()
    private var attemptsListFiltered = arrayListOf<Attempt>()

    private var skatersList = arrayListOf<Skater>()
    private var maleSkatersList = arrayListOf<Skater>()
    private var femaleSkatersList = arrayListOf<Skater>()

    private var season: Int = 20
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

        attemptsListViewModel.getAttemptsList(2020)

        standingAdapter = StandingAdapter(attemptsListFiltered, skatersList)
        rvStanding.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvStanding.adapter = standingAdapter

        rgSex.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            attemptsListViewModel.getAttemptListFiltered(skatersList)
            standingAdapter.notifyDataSetChanged()

            Log.i("MALE", "$maleSkatersList")
            Log.i("FEMALE", "$femaleSkatersList")

            skatersList.clear()
            if (radio.text == "Male"){//TODO HC
                sex = "m"
                skatersList.addAll(maleSkatersList)
            }
            else {
                sex = "f"
                skatersList.addAll(femaleSkatersList)
            }


            Log.i("skaters list", "$skatersList")

            attemptsListViewModel.getAttemptListFiltered(skatersList)

            Toast.makeText(context, " On checked change : ${radio.text}",
                    Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageButton>(R.id.ibUp).setOnClickListener {
            season++
            tvSeason.text = "Season 20$season-${season+1}"

        }
        view.findViewById<ImageButton>(R.id.ibDown).setOnClickListener {
            season--
            tvSeason.text = "Season 20$season-${season+1}"
        }

        attemptsListViewModel.attemptsList.observe(viewLifecycleOwner, {
            attemptsList.clear()
            attemptsList.addAll(it.attemptsList)
            Log.i("OBSERVE A", "standing fragment: $attemptsList" )

            attemptsListViewModel.getAttemptListFiltered(skatersList)
            standingAdapter.notifyDataSetChanged()
        })

        attemptsListViewModel.attemptsListFiltered.observe(viewLifecycleOwner, {
            attemptsListFiltered.clear()
            attemptsListFiltered.addAll(it.attemptsList)
            Log.i("OBSERVE AF", "standing fragment: $attemptsListFiltered" )

            standingAdapter.notifyDataSetChanged()
        })

        skatersListViewModel.maleSkatersList.observe(viewLifecycleOwner, {
            maleSkatersList.clear()
            maleSkatersList.addAll(it.skatersList)
            Log.i("OBSERVE S MALE", "standing fragment: $maleSkatersList" )

            standingAdapter.notifyDataSetChanged()

            if (sex == "m"){
                skatersList.clear()
                skatersList.addAll(maleSkatersList)
            }

            attemptsListViewModel.getAttemptListFiltered(skatersList)
        })

        skatersListViewModel.femaleSkatersList.observe(viewLifecycleOwner, {
            femaleSkatersList.clear()
            femaleSkatersList.addAll(it.skatersList)
            Log.i("OBSERVE S FEMALE", "standing fragment: $femaleSkatersList" )

            attemptsListViewModel.getAttemptListFiltered(skatersList)

            if (sex == "f"){
                skatersList.clear()
                skatersList.addAll(femaleSkatersList)
            }

            standingAdapter.notifyDataSetChanged()
        })
    }
}
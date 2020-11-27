package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.capstone.R
import com.example.capstone.model.Attempt
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.AttemptsListViewModel
import com.example.capstone.viewmodel.SkatersListViewModel

class WinnersFragment : Fragment() {


    private val attemptsViewModel: AttemptsListViewModel by activityViewModels()
    private val skatersViewModel: SkatersListViewModel by activityViewModels()

    private var attemptsList = arrayListOf<Attempt>()
    private var skatersList = arrayListOf<Skater>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_winners, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        attemptsViewModel.attemptsList.observe(viewLifecycleOwner, {
            val attemptsList = it
            Log.i("OBSERVE A", "home fragment: ${attemptsList}" )
        })

        skatersViewModel.skatersList.observe(viewLifecycleOwner, {
            skatersList.clear()
            skatersList = it.skatersList
            Log.i("OBSERVE S", "dashboard fragment: ${skatersList}" )
        })
    }
}

package com.example.capstone.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.capstone.R
import com.example.capstone.model.Attempt
import com.example.capstone.viewmodel.AttemptsListViewModel


class DashboardFragment : Fragment() {

    private val attemptsViewModel: AttemptsListViewModel by activityViewModels()

    private var attemptsList = arrayListOf<Attempt>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attemptsViewModel.getAttemptsList()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attemptsViewModel.attemptsList.observe(viewLifecycleOwner, {
            attemptsList.clear()
            attemptsList = it.attemptsList
            //make button visible and clickable
            Log.i("OBSERVE", "dashboard fragment: ${attemptsList}" )
        })
    }
}
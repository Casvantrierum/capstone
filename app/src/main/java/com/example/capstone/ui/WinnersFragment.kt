package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.capstone.R
import com.example.capstone.viewmodel.AttemptsListViewModel

class WinnersFragment : Fragment() {

    private val viewModel: AttemptsListViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_winners, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.attemptsList.observe(viewLifecycleOwner, {
            val attemptsList = it
            //make button visible and clickable
            Log.i("OBSERVE", "home fragment: ${attemptsList}" )
        })
    }
}

package com.example.capstone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.capstone.R
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.SkatersListViewModel
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.fragment_add_attempt.*


class AddAttemptFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val skatersListViewModel: SkatersListViewModel by activityViewModels()
    private var skatersList = arrayListOf<Skater>()
    var list_of_items = skatersList

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        val skater = parent.selectedItem as Skater
        Toast.makeText(
            parent.context,
            "ID : ${skater.id} | name: ${skater.name} | sex: ${skater.sex}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_attempt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerSkater.onItemSelectedListener = this


        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerSkater!!.adapter = aa


        skatersListViewModel.allSkatersList.observe(viewLifecycleOwner, {
            skatersList.clear()
            skatersList.addAll(it.skatersList)
            aa.notifyDataSetChanged()
        })

    }
}
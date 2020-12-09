package com.example.capstone.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.capstone.R
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.SkatersListViewModel
import kotlinx.android.synthetic.main.fragment_add_attempt.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddAttemptFragment : Fragment(), AdapterView.OnItemSelectedListener{

    private val skatersListViewModel: SkatersListViewModel by activityViewModels()
    private var skatersList = arrayListOf<Skater>()
    private val emptySkater = Skater(0, "New skater", "m", null)
    private lateinit var selectedSkater: Skater

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        val selectedSkater = parent.selectedItem as Skater
        if (selectedSkater.id != 0){
            etName.setText(selectedSkater.name)
            etSkitsId.setText(selectedSkater.id.toString())

            if (selectedSkater.sex == "f") rbFemale.isChecked //TODO HC?
            else rbMale.isChecked

            etName.isEnabled = false;
            rbMale.isEnabled = false;
            rbFemale.isEnabled = false;
            etSkitsId.isEnabled = false;
        }
        else {
            etName.setText("Name")//TODO hc
            etSkitsId.setText("SKITS ID")//TODO hc

            etName.isEnabled = true;
            rbMale.isEnabled = true;
            rbFemale.isEnabled = true;
            etSkitsId.isEnabled = true;
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerSkater.onItemSelectedListener = this


        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, skatersList)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerSkater!!.adapter = aa
        
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            etDate.setText("$dayOfMonth-$monthOfYear-$year")

        }, year, month, day)

        btnPickDate.setOnClickListener { dpd.show() }


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)
        etDate.setText(formatted)


        skatersListViewModel.allSkatersList.observe(viewLifecycleOwner, {
            skatersList.clear()
            skatersList.add(emptySkater)
            skatersList.addAll(it.skatersList)
            aa.notifyDataSetChanged()
        })
    }
}

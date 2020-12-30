package com.example.capstone.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.capstone.R
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.AddViewModel
import com.example.capstone.viewmodel.SkatersListViewModel
import kotlinx.android.synthetic.main.fragment_add_attempt.*
import kotlinx.android.synthetic.main.fragment_add_attempt.rbFemale
import kotlinx.android.synthetic.main.fragment_add_attempt.rbMale
import kotlinx.android.synthetic.main.fragment_add_attempt.rgSex
import java.util.*


class AddAttemptFragment : Fragment(), AdapterView.OnItemSelectedListener{

    private val skatersListViewModel: SkatersListViewModel by activityViewModels()
    private val addViewModel: AddViewModel by viewModels()

    private var skatersList = arrayListOf<Skater>()
    private val emptySkater = Skater(0, "New skater", "", "m", null)//TODO hc
    private  var selectedSkater = emptySkater

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        selectedSkater = parent.selectedItem as Skater
        if (selectedSkater.id != 0){
            etFirstname.setText(selectedSkater.firstname)
            etLastname.setText(selectedSkater.lastname)
            etSkitsId.setText(selectedSkater.id.toString())

            if (selectedSkater.sex == getString(R.string.female_short)) rbFemale.isChecked
            else rbMale.isChecked

            etFirstname.isEnabled = false
            etLastname.isEnabled = false
            rbMale.isEnabled = false
            rbFemale.isEnabled = false
            etSkitsId.isEnabled = false
        }
        else {
            etFirstname.clearComposingText()
            etFirstname.text = null
            etLastname.text = null
            etSkitsId.text= null

            etFirstname.isEnabled = true
            etLastname.isEnabled = true
            rbMale.isEnabled = true
            rbFemale.isEnabled = true
            etSkitsId.isEnabled = true
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

        pbAdd.visibility = View.INVISIBLE

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

        //set current Date in the date field
        etDateYear.setText(year.toString())
        etDateMonth.setText((month+ 1).toString())//months start at 0
        etDateDay.setText(day.toString())

        val dpd = DatePickerDialog(requireActivity(), { _, yearSelected, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            etDateDay.setText(dayOfMonth.toString())
            etDateMonth.setText((monthOfYear+ 1).toString())
            etDateYear.setText(yearSelected.toString())
        }, year, month, day)

        btnAdd.setOnClickListener {
            if(checkInput()){
                btnAdd.isEnabled = false
                var sex = getString(R.string.female_short)
                if (selectedSkater.id == 0){
                    val id = rgSex.checkedRadioButtonId
                    if (id == R.id.rbMale){
                        sex = getString(R.string.male_short)
                    }
                }
                else sex = selectedSkater.sex

                addViewModel.addSkater(
                        (selectedSkater.id == 0),
                        etFirstname.text.toString(),
                        etLastname.text.toString(),
                        sex,
                        etSkitsId.text.toString().toInt(),
                        etTime.text.toString(),
                        etWeather.text.toString(),
                        etDateDay.text.toString(),
                        etDateMonth.text.toString(),
                        etDateYear.text.toString())

            }
        }

        btnPickDate.setOnClickListener { dpd.show() }


        skatersListViewModel.allSkatersList.observe(viewLifecycleOwner, {
            skatersList.clear()
            skatersList.add(emptySkater)
            skatersList.addAll(it.skatersList)
            aa.notifyDataSetChanged()
        })

        addViewModel.fetching.observe(viewLifecycleOwner, {
            if (it) {
                pbAdd.visibility = View.VISIBLE
            } else {
                pbAdd.visibility = View.INVISIBLE
                btnAdd.isEnabled = true
            }
        })

        addViewModel.errorText.observe(viewLifecycleOwner, {
            Log.i("ERROR", "errorText: $it")
            Toast.makeText(
                    context, it,
                    Toast.LENGTH_LONG
            ).show()
        })

        addViewModel.createSuccess.observe(viewLifecycleOwner, {
            Log.i("createSucces", "value: $it")
            if (it)
                findNavController().navigate(R.id.action_addAttemptFragment_to_navigation_standing)
        })
    }

    private fun checkInput(): Boolean{
        var wrongElements = ""
        if (selectedSkater.id == 0){
            if (etFirstname.text.toString() == "") wrongElements += " ${getString(R.string.input_firstname)},"
            if (etLastname.text.toString() == "") wrongElements += " ${getString(R.string.input_lastname)},"
            if (etSkitsId.text.toString() == "" || etSkitsId.text.toString().toInt() <= 0)
                wrongElements += " ${getString(R.string.input_skits_id)},"
        }


        val day = etDateDay.text.toString()
        val month = etDateMonth.text.toString()
        val year = etDateYear.text.toString()
        if (day == "" || day.toInt() <= 0 || day.toInt() > 31)
            wrongElements += " ${getString(R.string.input_day)},"
        if (month == "" || month.toInt() <= 0 || month.toInt() > 12)
            wrongElements += " ${getString(R.string.input_month)},"
        if (year == "")
            wrongElements += " ${getString(R.string.input_year)},"

        val timeRegex = "[0-9]?[0-9].[0-5][0-9]".toRegex()
        if (!etTime.text.toString().matches(timeRegex)) wrongElements += " ${getString(R.string.input_time)},"

        return if (wrongElements == ""){
            true
        }
        else {
            wrongElements = wrongElements.substring(0, wrongElements.length -1) //strip last comma
            Toast.makeText(
                    requireActivity(),
                    getString(R.string.incorrect_input, wrongElements),
                    Toast.LENGTH_LONG
            ).show()
            false
        }
    }
}

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
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.capstone.R
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.AddViewModel
import com.example.capstone.viewmodel.SkatersListViewModel
import kotlinx.android.synthetic.main.fragment_add_attempt.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddAttemptFragment : Fragment(), AdapterView.OnItemSelectedListener{

    private val skatersListViewModel: SkatersListViewModel by activityViewModels()
    private val addViewModel: AddViewModel by viewModels()
    private var skatersList = arrayListOf<Skater>()
    private val emptySkater = Skater(0, "New skater", "m", null)//TODO hc
    private  var selectedSkater = emptySkater

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        selectedSkater = parent.selectedItem as Skater
        if (selectedSkater.id != 0){
            etFirstname.setText(selectedSkater.name)
            etSkitsId.setText(selectedSkater.id.toString())

            if (selectedSkater.sex == "f") rbFemale.isChecked //TODO HC?
            else rbMale.isChecked

            etFirstname.isEnabled = false;
            etLastname.isEnabled = false;
            rbMale.isEnabled = false;
            rbFemale.isEnabled = false;
            etSkitsId.isEnabled = false;
        }
        else {
            etFirstname.setText("Name")//TODO hc
            etSkitsId.setText("SKITS ID")//TODO hc

            etFirstname.isEnabled = true;
            etLastname.isEnabled = true;
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

        //set current Date in the date field
        etDateYear.setText(year.toString())
        etDateMonth.setText((month+ 1).toString())//months start at 0
        etDateDay.setText(day.toString())

        val dpd = DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            etDateDay.setText(dayOfMonth.toString())
            etDateMonth.setText((monthOfYear+ 1).toString())
            etDateYear.setText(year.toString())
        }, year, month, day)

        btnAdd.setOnClickListener {
            var sex = "f"
            if (selectedSkater.id == 0){
                val id = rgSex.checkedRadioButtonId
                if (id == R.id.rbMale){//TODO HC
                    sex = "m"
                }
            }
            else sex = selectedSkater.sex

            Log.i("PROCEED", "with selectedSkater: $selectedSkater")

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

        btnPickDate.setOnClickListener { dpd.show() }


        skatersListViewModel.allSkatersList.observe(viewLifecycleOwner, {
            skatersList.clear()
            skatersList.add(emptySkater)
            skatersList.addAll(it.skatersList)
            aa.notifyDataSetChanged()
        })
    }
}

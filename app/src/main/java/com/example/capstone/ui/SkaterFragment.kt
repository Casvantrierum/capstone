package com.example.capstone.ui


import android.content.ComponentName
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.adapter.PersonalRecordAdapter
import com.example.capstone.adapter.SkaterResultAdapter
import com.example.capstone.model.Attempt
import com.example.capstone.model.SSRPersonalRecord
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.AttemptsListViewModel
import com.example.capstone.viewmodel.SSRViewModel
import com.example.capstone.viewmodel.SkaterViewModel
import kotlinx.android.synthetic.main.fragment_skater.*
import kotlinx.android.synthetic.main.item_standing.tvName
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class SkaterFragment : Fragment() {

    private val attemptsListViewModel: AttemptsListViewModel by viewModels()
    private val skaterViewModel: SkaterViewModel by activityViewModels()
    private val ssrViewModel: SSRViewModel by viewModels()

    private lateinit var skater: Skater
    private var attemptsList = arrayListOf<Attempt>()

    private var personalRecords = arrayListOf<SSRPersonalRecord>()

    val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";

    private var mCustomTabsServiceConnection: CustomTabsServiceConnection? = null
    private var mClient: CustomTabsClient? = null
    private var mCustomTabsSession: CustomTabsSession? = null

    private lateinit var resultAdapter: SkaterResultAdapter
    private lateinit var personalRecordAdapter: PersonalRecordAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_skater, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date1: Long = LocalDate.parse("14-10-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val date2: Long = LocalDate.parse("18-10-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val date3: Long = LocalDate.parse("14-11-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()


//        val aaChartModel : AAChartModel = AAChartModel()
//            .chartType(AAChartType.Line)
//            .title("title")
//            .subtitle("subtitle")
//            .backgroundColor("#FFFFFF")
//            .dataLabelsEnabled(true)
//            .series(arrayOf(
//                    AASeriesElement()
//                            .name("Tokyo")
//                            .data(arrayOf(
//                                    arrayOf(date1, 13.40),
//                                    arrayOf(date2, 13.59),
//                                    arrayOf(date3, 14.00)
//                            ))
//            ))
//
//
//        //The chart view object calls the instance object of AAChartModel and draws the final graphic
//        val aaChartView : AAChartView = aa_chart_view
//        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        resultAdapter = SkaterResultAdapter(attemptsList)
        rvResults.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvResults.adapter = resultAdapter

        personalRecordAdapter = PersonalRecordAdapter(personalRecords)
        rvPersonalRecords.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvPersonalRecords.adapter = personalRecordAdapter

        mCustomTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(componentName: ComponentName, customTabsClient: CustomTabsClient) {
                //Pre-warming
                mClient = customTabsClient
                mClient?.warmup(0L)
                mCustomTabsSession = mClient?.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mClient = null
            }
        }
        activity?.let { CustomTabsClient.bindCustomTabsService(it, CUSTOM_TAB_PACKAGE_NAME,
                mCustomTabsServiceConnection as CustomTabsServiceConnection
        ) }

        tvSkitsLink.setOnClickListener {
            val customTabsIntent = CustomTabsIntent.Builder(mCustomTabsSession)
                .setShowTitle(true)
                .build()

            context?.let { it1 -> customTabsIntent.launchUrl(it1, Uri.parse(getString(R.string.skits_site_member, skater.id))) }
        }

        skaterViewModel.skater.observe(viewLifecycleOwner, {
            skater = it
            tvName.text = it.toString()

            attemptsListViewModel.getAttemptsListSkater(skater.id)
            ssrViewModel.getSSRPersonalRecord(skater)
        })

        attemptsListViewModel.attemptsList.observe(viewLifecycleOwner, {
            attemptsList.clear()
            attemptsList.addAll(it.attemptsList)

            resultAdapter.notifyDataSetChanged()
        })

        ssrViewModel.results.observe(viewLifecycleOwner) {
            personalRecords.clear()
            personalRecords.addAll(it.records)
            personalRecordAdapter.notifyDataSetChanged()
        }
    }

}
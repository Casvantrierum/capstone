package com.example.capstone.ui


import android.annotation.SuppressLint
import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.capstone.R
import com.example.capstone.model.Skater
import com.example.capstone.viewmodel.SkaterViewModel
import kotlinx.android.synthetic.main.fragment_skater.*
import kotlinx.android.synthetic.main.item_standing.tvName


class SkaterFragment : Fragment() {

    private val skaterViewModel: SkaterViewModel by activityViewModels()
    private lateinit var skater: Skater


    val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";

    private var mCustomTabsServiceConnection: CustomTabsServiceConnection? = null
    private var mClient: CustomTabsClient? = null
    private var mCustomTabsSession: CustomTabsSession? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_skater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            tvName.text = it.name
        })
    }

}
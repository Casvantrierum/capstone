package com.example.capstone

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.capstone.viewmodel.AttemptsListViewModel
import com.example.capstone.viewmodel.SkatersListViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_winners, R.id.navigation_standing, R.id.navigation_information))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        FirebaseFirestore.setLoggingEnabled(true)
        FirebaseApp.initializeApp(this)

        val attemptsListViewModel: AttemptsListViewModel = ViewModelProvider(this).get(AttemptsListViewModel::class.java)
        attemptsListViewModel.getAttemptsList(null)

        val skatersListViewModel: SkatersListViewModel = ViewModelProvider(this).get(SkatersListViewModel::class.java)
        skatersListViewModel.getSkatersList()
    }
}
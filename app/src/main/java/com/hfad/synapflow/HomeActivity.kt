package com.hfad.synapflow

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.hfad.synapflow.analytics.Figures
import com.hfad.synapflow.analytics.analyticsMain
public val fsdb = FirestoreData()
public val plots = Figures()

class HomeActivity : AppCompatActivity() {

    private lateinit var fb: FirebaseAuth
    //val fsdb = FirestoreData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        title = "Synapflow"

        //Request user permissions upon accessing via Login/Skip Login
        permissionRequest()

        // Create notification channel
        createNotificationChannel()

        val calendarFragment = CalendarFragment()
        val analyticsFragment = analyticsMain()
        val rewardsFragment = Rewards()
        val timerFragment = TimerFragment()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        fsdb.checkUserExists()
        if (fsdb.getUserExists()){
            fsdb.getTaskMap()
            plots.refreshData()
        }
        calendarFragment.getTasksForCalender()
        // Navbar navigation
        bottomNav.setOnItemSelectedListener {
            item -> when(item.itemId) {
                R.id.nav_home -> {
                     // Comment this out if you see it:
                    //fsdb.getCompletionDates()
                    changeFragment(timerFragment)
                    true
                }
                R.id.nav_calendar -> {
                    changeFragment(calendarFragment)
                    true
                }
                R.id.nav_analytics -> {
                    plots.refreshData()
                    changeFragment(analyticsFragment)
                    true
                }
                R.id.nav_rewards -> {
                    changeFragment(rewardsFragment)
                    true
                }
                else -> false
            }
        }
        // Go to add task fragment when add button is clicked
        bottomNav.findViewById<FloatingActionButton>(R.id.nav_add_item).setOnClickListener {
            bottomNav.isSelected = false
            openAddTaskFragment()
        }
    }
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Synapflow"
            val descriptionText = "Synapflow Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("C10", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    // Separate function to open add task fragment so the fragment gets reset when returning
    private fun openAddTaskFragment() {
        val addTaskFragment = AddTaskFragment()
        // Check if fragment is running, if so then remove it before adding it again
        if(addTaskFragment.isAdded) {
            supportFragmentManager.beginTransaction().remove(addTaskFragment).commit()
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, addTaskFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun permissionRequest() {
        var permissionList = mutableListOf<String>()
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)) {
            permissionList.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), 0)
        }
    }
    //When user grants or denies permission, this function is called
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivityPerms", "${permissions[i]} granted.")
                }
            }
        }
    }
}
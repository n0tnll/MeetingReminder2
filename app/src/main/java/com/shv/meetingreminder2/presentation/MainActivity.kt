package com.shv.meetingreminder2.presentation

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.shv.meetingreminder2.R
import com.shv.meetingreminder2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkPermission()
        setupToolBar()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission() {
        val permissionsGranted = arrayListOf(
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ),
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SCHEDULE_EXACT_ALARM
            )
        ).any { it == PackageManager.PERMISSION_GRANTED }

        if (!permissionsGranted) {
            requestNotificationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.SCHEDULE_EXACT_ALARM
            ),
            SEND_PUSH_NOTIFICATION_RC
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == SEND_PUSH_NOTIFICATION_RC && grantResults.isNotEmpty()) {
            val permissionsGranted = grantResults.any { it == PackageManager.PERMISSION_GRANTED }
            if (!permissionsGranted) {
                Toast.makeText(
                    this,
                    getString(R.string.notifications_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setupToolBar() {
        setSupportActionBar(binding.tbApp)
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.mainFragmentContainer.id) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.tbApp.setupWithNavController(navController, appBarConfiguration)
    }

    companion object {
        private const val SEND_PUSH_NOTIFICATION_RC = 12
    }
}
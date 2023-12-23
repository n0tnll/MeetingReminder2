package com.shv.meetingreminder2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.shv.meetingreminder2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolBar()
    }

    private fun setupToolBar() {
        setSupportActionBar(binding.tbApp)
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.mainFragmentContainer.id) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.tbApp.setupWithNavController(navController, appBarConfiguration)
    }
}
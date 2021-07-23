package com.example.code.ui.activities

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.code.R
import com.example.code.databinding.ActivityApplicationBinding
import com.example.code.ui.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ApplicationActivity : BaseActivity<ActivityApplicationBinding>(ActivityApplicationBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration = AppBarConfiguration(
                    setOf(
                            R.id.navigation_internal_storage_gallery_fragment,
                            R.id.navigation_external_storage_gallery_fragment,
                            R.id.navigation_camera_fragment
                    )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
            navView.background = null
            navView.menu.getItem(1).isEnabled = false
        }

    }

}
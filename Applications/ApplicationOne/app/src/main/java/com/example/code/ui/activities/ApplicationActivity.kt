package com.example.code.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.code.R
import com.example.code.databinding.ActivityApplicationBinding
import com.example.code.ui.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ApplicationActivity : BaseActivity<ActivityApplicationBinding>(ActivityApplicationBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        binding.apply {

            val appBarConfiguration = AppBarConfiguration(
                    setOf(
                            R.id.navigation_internal_storage_gallery_fragment,
                            R.id.navigation_external_storage_gallery_fragment,
                            R.id.navigation_camera_fragment
                    )
            )
            setupActionBarWithNavController( navHostFragment.navController, appBarConfiguration)
            navView.setupWithNavController( navHostFragment.navController)
            navView.background = null
            navView.menu.getItem(1).isEnabled = false
        }



        binding.cameraId.setOnClickListener {
            Toast.makeText(this@ApplicationActivity,"Launch Camera",Toast.LENGTH_LONG).show()
        }

    }

}
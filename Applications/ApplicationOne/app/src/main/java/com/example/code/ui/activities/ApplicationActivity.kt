package com.example.code.ui.activities

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.code.R
import com.example.code.databinding.ActivityApplicationBinding
import com.example.code.ui.base.BaseActivity
import com.example.code.ui.state.ViewResult
import com.example.code.vm.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApplicationActivity : BaseActivity<ActivityApplicationBinding>(ActivityApplicationBinding::inflate) {

    private val sharedViewModel by viewModel<SharedViewModel>()

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

        setupObserver()

        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.apply {
            cameraId.setOnClickListener {
                captureImage()
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            sharedViewModel.view.collect {
                setViewState(it)
            }
        }
    }

    private fun setViewState(it: ViewResult) {
        when (it) {
            is ViewResult.TakePictureFromCamera.CapturePhoto -> captureImage()
        }
    }

    private fun captureImage() {
        binding.cameraId.setOnClickListener {
            Toast.makeText(this@ApplicationActivity,"Launch Camera",Toast.LENGTH_LONG).show()
            launchSelectionAlert()
        }
    }

    private fun launchSelectionAlert() {
        MaterialAlertDialogBuilder(this@ApplicationActivity)
            .setMessage(R.string.select_the_mode_of_storage)
            .setPositiveButton(R.string.private_true){ _, _ ->
                Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(R.string.private_false){ _, _ ->
                Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
            }
            .show()
    }

}
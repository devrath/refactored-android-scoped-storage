package com.example.code.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import java.util.*

class ApplicationActivity :
    BaseActivity<ActivityApplicationBinding>(ActivityApplicationBinding::inflate) {

    private val sharedViewModel by viewModel<SharedViewModel>()
    private var isPrivate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpNavView()
        setupObserver()
        setOnClickListener()
    }

    private fun setUpNavView() {
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
            setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
            navView.setupWithNavController(navHostFragment.navController)
            navView.background = null
            navView.menu.getItem(1).isEnabled = false
        }
    }

    private fun setOnClickListener() {
        binding.apply {
            cameraId.setOnClickListener {
                Toast.makeText(this@ApplicationActivity, "Launch Camera", Toast.LENGTH_LONG).show()
                launchSelectionAlert()
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
            //is ViewResult.TakePictureFromCamera.CapturePhoto -> captureImage()
        }
    }

    private fun launchSelectionAlert() {
        MaterialAlertDialogBuilder(this@ApplicationActivity)
            .setMessage(R.string.select_the_mode_of_storage)
            .setPositiveButton(R.string.private_true) { _, _ -> launchCamera(isPrivate = true) }
            .setNegativeButton(R.string.private_false) { _, _ -> launchCamera(isPrivate = false) }
            .show()
    }

    private fun launchCamera(isPrivate: Boolean) {
        this.isPrivate = isPrivate
        takePhoto.launch()
    }

    /**
     * ON-ACTIVITY-RESULT
     */
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        if (isPrivate) {
            sharedViewModel.loadImagesFromInternalStorage(bitmap = it)
        }
    }

}
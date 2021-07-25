package com.example.code.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
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
        // Update the flag in the view model
        sharedViewModel.isPrivate = isPrivate

        when {
            sharedViewModel.isPrivate -> takePhoto.launch()
            else -> {
                registerForExternalStoragePermissions()
                updateOrRequestPermissions()
            }
        }

    }

    /**
     * This function is used to update or ask for the permission
     * REQUEST FOR PERMISSION:-> If the permission is not available, launch for permission request and update the flag
     * UPDATE THE PERMISSION:--> If the permission is already available, still just update the flag without launching the request
     */
    private fun updateOrRequestPermissions() {
        // For the read permission
        val hasReadPermission = ContextCompat.checkSelfPermission(this@ApplicationActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        // For the write permission
        val hasWritePermission = ContextCompat.checkSelfPermission(this@ApplicationActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        // Check for the android version, Q = Android_29
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        sharedViewModel.readPermissionGranted = hasReadPermission
        // Below, Q = Android_29 it has to be true to proceed and
        // if its 29 above minSdk29 will be true and even if the hasWritePermission is false  still we proceed
        sharedViewModel.writePermissionGranted = hasWritePermission || minSdk29
        // Array to store the list of permissions
        val permissionsToRequest = mutableListOf<String>()
        // Add the permission to list if not granted
        if (!sharedViewModel.writePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        // Add the permission to list if not granted
        if (!sharedViewModel.readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        // Launch the permissions by passing the permissions array
        if (permissionsToRequest.isNotEmpty()) {
            sharedViewModel.permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }


    /**
     * ************************************ ON-ACTIVITY-RESULT *************************************
     */
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        it?.let { imageBitmap ->
            when {
                sharedViewModel.isPrivate -> sharedViewModel.storeImageToInternalStorage(bitmap = imageBitmap)
                sharedViewModel.writePermissionGranted -> sharedViewModel.storeImageToExternalStorage(bitmap = imageBitmap)
            }
        }
    }

    private fun registerForExternalStoragePermissions() {
        sharedViewModel.permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            sharedViewModel.readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: sharedViewModel.readPermissionGranted
            sharedViewModel.writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: sharedViewModel.writePermissionGranted
            // Load something from external storage
            
        }
    }
    /**
     * ************************************ ON-ACTIVITY-RESULT *************************************
     */

}
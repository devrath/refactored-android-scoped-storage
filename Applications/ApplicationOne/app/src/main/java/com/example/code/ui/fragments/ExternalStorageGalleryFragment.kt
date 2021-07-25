package com.example.code.ui.fragments

import android.database.ContentObserver
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.code.databinding.FragmentExternalStorageGalleryBinding
import com.example.code.ui.adapters.SharedPhotoAdapter
import com.example.code.ui.base.BaseFragment
import com.example.code.ui.state.ViewResult
import com.example.code.utils.ExternalStorageUtils.loadPhotosFromExternalStorage
import com.example.code.utils.ExternalStorageUtils.savePhotoToExternalStorage
import com.example.code.vm.SharedViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ExternalStorageGalleryFragment : BaseFragment<FragmentExternalStorageGalleryBinding>(FragmentExternalStorageGalleryBinding::inflate) {

    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private lateinit var externalStoragePhotoAdapter: SharedPhotoAdapter

    private lateinit var contentObserver: ContentObserver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupExternalStorageRecyclerView()
        setAdapter()
        initContentObserver()
        loadPhotosFromExternalStorageIntoRecyclerView()
    }

    /**
     * Set the adapter of the list view
     */
    private fun setAdapter() {
        externalStoragePhotoAdapter = SharedPhotoAdapter {
            // ------> Handle on click of the list view
            lifecycleScope.launch {
                // Set the URI to be deleted in the shared view model to access in the activity
                sharedViewModel.deletedImageUri = it.contentUri
                sharedViewModel.deleteImageFromExternalStorage()
            }
        }
    }

    /**
     * This content observer is needed to refresh the recycler view once a image is deleted from the storage
     */
    private fun initContentObserver() {
        contentObserver = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                if(sharedViewModel.readPermissionGranted) {
                    loadPhotosFromExternalStorageIntoRecyclerView()
                }
            }
        }
        requireActivity().contentResolver?.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                contentObserver
        )
    }

    private fun setupExternalStorageRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = externalStoragePhotoAdapter
        layoutManager = GridLayoutManager(activity, 3)
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
            is ViewResult.LoadImagesFromExternalStorage -> refreshList(it.fileName, it.bitmap)
        }
    }

    private fun refreshList(fileName: String, bitmap: Bitmap) {
        lifecycleScope.launch {
            val isSavedSuccessfully = savePhotoToExternalStorage(fileName, bitmap,requireActivity())
            if (isSavedSuccessfully) {
                loadPhotosFromExternalStorageIntoRecyclerView()
                sharedViewModel.displayAlert(message = "Photo saved successfully")
            } else {
                sharedViewModel.displayAlert(message = "Failed to save photo")
            }
        }
    }

    private fun loadPhotosFromExternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val photos = loadPhotosFromExternalStorage(requireActivity())
            externalStoragePhotoAdapter.submitList(photos)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().contentResolver.unregisterContentObserver(contentObserver)
    }
}
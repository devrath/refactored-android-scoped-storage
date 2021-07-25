package com.example.code.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.code.databinding.FragmentInternalStorageGalleryBinding
import com.example.code.ui.adapters.InternalStoragePhotoAdapter
import com.example.code.ui.base.BaseFragment
import com.example.code.ui.state.ViewResult
import com.example.code.utils.InternalStorageUtils.deletePhotoFromInternalStorage
import com.example.code.utils.InternalStorageUtils.loadPhotosFromInternalStorage
import com.example.code.utils.InternalStorageUtils.savePhotoToInternalStorage
import com.example.code.vm.SharedViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class InternalStorageGalleryFragment :
    BaseFragment<FragmentInternalStorageGalleryBinding>(FragmentInternalStorageGalleryBinding::inflate) {

    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            lifecycleScope.launch {
                val isDeletionSuccessful = deletePhotoFromInternalStorage(it.name,requireActivity())
                if(isDeletionSuccessful) {
                    loadPhotosFromInternalStorageIntoRecyclerView()
                    sharedViewModel.displayAlert(message = "Photo successfully deleted")
                } else {
                    sharedViewModel.displayAlert(message = "Failed to delete photo")
                }
            }
        }

        setupObserver()
        setupInternalStorageRecyclerView()
        loadPhotosFromInternalStorageIntoRecyclerView()
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
            is ViewResult.LoadImagesFromInternalStorage.Success -> refreshList(it.fileName,it.bitmap)
        }
    }

    private fun refreshList(fileName: String, bitmap: Bitmap) {
        lifecycleScope.launch {
            val isSavedSuccessfully = savePhotoToInternalStorage(fileName,bitmap,requireActivity())
            if (isSavedSuccessfully) {
                loadPhotosFromInternalStorageIntoRecyclerView()
                Toast.makeText(activity, "Photo saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Failed to save photo", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupInternalStorageRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = internalStoragePhotoAdapter
        layoutManager = GridLayoutManager(activity, 3)
    }

    private fun loadPhotosFromInternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val photos = loadPhotosFromInternalStorage(requireActivity())
            internalStoragePhotoAdapter.submitList(photos)
        }
    }

}
package com.example.code.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.code.databinding.FragmentInternalStorageGalleryBinding
import com.example.code.models.InternalStoragePhoto
import com.example.code.ui.adapters.InternalStoragePhotoAdapter
import com.example.code.ui.base.BaseFragment
import com.example.code.ui.state.ViewResult
import com.example.code.vm.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.IOException
import java.util.*

class InternalStorageGalleryFragment :
    BaseFragment<FragmentInternalStorageGalleryBinding>(FragmentInternalStorageGalleryBinding::inflate) {

    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            lifecycleScope.launch {
                val isDeletionSuccessful = deletePhotoFromInternalStorage(it.name)
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
            val isSavedSuccessfully = savePhotoToInternalStorage(fileName,bitmap)
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
            val photos = loadPhotosFromInternalStorage()
            internalStoragePhotoAdapter.submitList(photos)
        }
    }

    /**
     * This function is used to delete the file from the mentioned file location
     * @param filename -> Name of the file to be deleted
     * @return true/false -> Based on successful and unsuccessful condition
     */
    private suspend  fun deletePhotoFromInternalStorage(filename: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                requireActivity().deleteFile(filename)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    /**
     * This function will load the list of photos from a internal storage
     * The function is a suspend function since it takes lot of time to load
     * @return List of files retrieved from the internal storage
     */
    private suspend fun loadPhotosFromInternalStorage(): List<InternalStoragePhoto> {
        /* Since it will take lot of time to load the photos from the internal storage,
         * lets use a IO dispatcher */
        return withContext(Dispatchers.IO) {
            // filesDir : -> Refers to the root directory of our internal storage
            // Here we get reference list to all files in root folder

            requireActivity().let { activity ->
                val files = activity.filesDir.listFiles()

                files?.filter { file ->
                    // If the file can be read, It is a file, It ends with .jpg
                    file.canRead() && file.isFile && file.name.endsWith(".jpg")
                }?.map {
                    // For the filtered files -> For each file get the bytes of individual file one by one
                    val bytes = it.readBytes()
                    // Convert the bytes to bitmap
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    // Return the model that comprise of file name and the bitmap
                    InternalStoragePhoto(it.name, bmp)
                } ?: listOf()
            }

        }
    }

    /**
     * Saving the photo in internal storage
     * @param filename - It is the name of the file we are storing
     * @param bmp - It is the bitmap used to create the file
     * **********************************************************************
     * @return true/false based on if the image is successfully stored or not
     */
    private suspend fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {
        // When we are dealing with the storage, It is better to wrap our code with try/catch block since many things might go wrong
        return withContext(Dispatchers.IO) {
            try {
                /* We use the openFileOutput to handle the streams, Bitmap is nothing but a chunk of data
                 * Use is a kotlin extension function we use for file handling, it helps us to close the stream after being used */
                requireActivity().openFileOutput("$filename.jpg", AppCompatActivity.MODE_PRIVATE).use { stream ->
                    // Returns true if the write is successful else it throws exception
                    if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        // Returns an exception in case of a failure
                        throw IOException("Couldn't save bitmap.")
                    }
                }
                true
            } catch(e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

}
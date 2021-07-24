package com.example.code.ui.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.code.databinding.ActivityInternalStorageImagesBinding
import com.example.code.models.InternalStoragePhoto
import com.example.code.ui.adapters.InternalStoragePhotoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class InternalStorageImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInternalStorageImagesBinding

    private lateinit var internalStoragePhotoAdapter: InternalStoragePhotoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInternalStorageImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        internalStoragePhotoAdapter = InternalStoragePhotoAdapter {
            val isDeletionSuccessful = deletePhotoFromInternalStorage(it.name)
            if(isDeletionSuccessful) {
                loadPhotosFromInternalStorageIntoRecyclerView()
                Toast.makeText(this, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete photo", Toast.LENGTH_SHORT).show()
            }
        }

       /* val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            val isPrivate = binding.switchPrivate.isChecked
            if(isPrivate) {
                val isSavedSuccessfully = savePhotoToInternalStorage(UUID.randomUUID().toString(), it)
                if(isSavedSuccessfully) {
                    loadPhotosFromInternalStorageIntoRecyclerView()
                    Toast.makeText(this, "Photo saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnTakePhoto.setOnClickListener {
            takePhoto.launch()
        }*/
        setupInternalStorageRecyclerView()
        loadPhotosFromInternalStorageIntoRecyclerView()
    }

    private fun setupInternalStorageRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = internalStoragePhotoAdapter
        //layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
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
    private fun deletePhotoFromInternalStorage(filename: String): Boolean {
        return try {
            deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            false
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
            val files = filesDir.listFiles()

            files?.filter {
                // If the file can be read, It is a file, It ends with .jpg
                it.canRead() && it.isFile && it.name.endsWith(".jpg")
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

    /**
     * Saving the photo in internal storage
     * @param filename - It is the name of the file we are storing
     * @param bmp - It is the bitmap used to create the file
     * **********************************************************************
     * @return true/false based on if the image is successfully stored or not
     */
    private fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {
        // When we are dealing with the storage, It is better to wrap our code with try/catch block since many things might go wrong
        return try {
            /* We use the openFileOutput to handle the streams, Bitmap is nothing but a chunk of data
             * Use is a kotlin extension function we use for file handling, it helps us to close the stream after being used */
            openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
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
package com.example.code.ui.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.code.R
import com.example.code.databinding.FragmentExternalStorageGalleryBinding
import com.example.code.databinding.FragmentInternalStorageGalleryBinding
import com.example.code.sdk29AndUp
import com.example.code.ui.adapters.SharedPhotoAdapter
import com.example.code.ui.base.BaseFragment
import com.example.code.ui.state.ViewResult
import com.example.code.vm.SharedViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.IOException

class ExternalStorageGalleryFragment :
    BaseFragment<FragmentExternalStorageGalleryBinding>(FragmentExternalStorageGalleryBinding::inflate) {

    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private lateinit var externalStoragePhotoAdapter: SharedPhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        externalStoragePhotoAdapter = SharedPhotoAdapter {

        }



        setupObserver()
        //setupInternalStorageRecyclerView()
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
            is ViewResult.LoadImagesFromExternalStorage.Success -> refreshList(it.fileName,it.bitmap)
        }
    }

    private fun refreshList(fileName: String, bitmap: Bitmap) {
        val isSavedSuccessfully = savePhotoToExternalStorage(fileName,bitmap)
        if (isSavedSuccessfully) {
            //loadPhotosFromInternalStorageIntoRecyclerView()
            Toast.makeText(activity, "Photo saved successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Failed to save photo", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Saving the photo in external storage
     * @param filename - It is the name of the file we are storing
     * @param bmp - It is the bitmap used to create the file
     * **********************************************************************
     * @return true/false based on if the image is successfully stored or not
     */
    private fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {
        /* ******************************************************************
         * MEDIA STORE:-> It is a huge data base with all media files and corresponding meta data
         * USAGE of media store:-> With the media store we can get the collection of images and add image to that collection
         * URI:-> Address where the image is stored
         * USAGE of uri:-> Getting the uri is different below API 29 and different above 29 remember
         * ******************************************************************
         * */


        val imageCollection = sdk29AndUp { // For the SDK 29 and above use the media store to get the URI
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: // For the SDK below the 29 use the external URI
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        /**
         * We not just want to save the bitmap but also the meta data along with it
         * These data can be used by us and other apps -> For that we use content values
         * CONTENT VALUES: They are similar to bundles where we can
         * **/
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }

        return try {

            requireActivity().let{
                // We use content resolver to insert all the media store entries or read them.

                // Insert at a particular URI and at that Uri we have to add all the content values
                it.contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                    /* We use the openFileOutput to handle the streams, Bitmap is nothing but a chunk of data
                     * Use is a kotlin extension function we use for file handling, it helps us to close the stream after being used */
                    activity?.contentResolver?.openOutputStream(uri).use { outputStream ->
                        // Returns true if the write is successful else it throws exception
                        if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                            // Returns an exception in case of a failure
                            throw IOException("Couldn't save bitmap")
                        }

                    }
                } ?: throw IOException("Couldn't create MediaStore entry")
            }

            true
        } catch(e: IOException) {
            e.printStackTrace()
            false
        }
    }

}
package com.example.code.ui.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            is ViewResult.LoadImagesFromInternalStorage.Success -> {
                // refreshList(it.fileName,it.bitmap)
            }
        }
    }

    private fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {

        val imageCollection = sdk29AndUp { MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }

        return try {

            requireActivity().let{
                it.contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                    activity?.contentResolver?.openOutputStream(uri).use { outputStream ->
                        if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
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
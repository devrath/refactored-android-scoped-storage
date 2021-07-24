package com.example.code.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.code.R
import com.example.code.databinding.FragmentExternalStorageGalleryBinding
import com.example.code.databinding.FragmentInternalStorageGalleryBinding
import com.example.code.ui.base.BaseFragment
import com.example.code.vm.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ExternalStorageGalleryFragment :
    BaseFragment<FragmentExternalStorageGalleryBinding>(FragmentExternalStorageGalleryBinding::inflate) {

    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.test.text = "ExternalStorageGalleryFragment"
    }
}
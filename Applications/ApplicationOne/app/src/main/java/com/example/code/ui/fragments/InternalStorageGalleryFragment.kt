package com.example.code.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.code.databinding.FragmentInternalStorageGalleryBinding
import com.example.code.ui.base.BaseFragment
import com.example.code.vm.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class InternalStorageGalleryFragment :
    BaseFragment<FragmentInternalStorageGalleryBinding>(FragmentInternalStorageGalleryBinding::inflate) {

    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.test.text = "InternalStorageGalleryFragment"
    }

}
package com.example.code.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.code.databinding.FragmentInternalStorageGalleryBinding
import com.example.code.ui.base.BaseFragment

class InternalStorageGalleryFragment :
    BaseFragment<FragmentInternalStorageGalleryBinding>(FragmentInternalStorageGalleryBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.test.text = "Hello view binding"
    }

}
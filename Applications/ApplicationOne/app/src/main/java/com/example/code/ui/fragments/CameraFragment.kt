package com.example.code.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.code.R
import com.example.code.databinding.FragmentCameraBinding
import com.example.code.databinding.FragmentInternalStorageGalleryBinding
import com.example.code.ui.base.BaseFragment

class CameraFragment :
    BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding.test.text = "Hello view binding"
    }

}
package com.example.code

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.code.databinding.ActivityInternalStorageImagesBinding

class InternalStorageImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInternalStorageImagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInternalStorageImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
package com.example.demo.demos.demoOne

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demo.databinding.ActivityDemoOneBinding
import com.example.demo.databinding.ActivitySelectionBinding

class DemoOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoOneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.apply {

        }
    }

}
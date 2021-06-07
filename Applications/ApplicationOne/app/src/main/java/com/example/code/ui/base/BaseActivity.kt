package com.example.code.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<VIEW_BINDING: ViewBinding>(
    private val inflate: InflateActivity<VIEW_BINDING>
) : AppCompatActivity() {

    lateinit var binding : VIEW_BINDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
    }
}

typealias InflateActivity<T> = (LayoutInflater) -> T
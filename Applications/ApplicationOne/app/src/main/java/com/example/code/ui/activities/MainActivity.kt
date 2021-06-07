package com.example.code.ui.activities

import android.os.Bundle
import com.example.code.databinding.ActivityMainBinding
import com.example.code.openActivity
import com.example.code.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            btnInternalStorageImgId.setOnClickListener {
                openActivity(InternalStorageImagesActivity::class.java)
            }
        }
    }

}

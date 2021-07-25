package com.example.code.ui.state

import android.graphics.Bitmap

sealed class ViewResult {
    object InitialState : ViewResult()
    data class AlertMessage(val message : String) : ViewResult()
    data class LoadImagesFromInternalStorage(val fileName : String, val bitmap: Bitmap) : ViewResult()
    data class LoadImagesFromExternalStorage(val fileName : String, val bitmap: Bitmap) : ViewResult()
}
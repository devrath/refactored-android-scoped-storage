package com.example.code.ui.state

import android.graphics.Bitmap

sealed class ViewResult {

    object InitialState : ViewResult()

    data class ErrorMessage(val error : String) : ViewResult()

    sealed class TakePictureFromCamera: ViewResult(){
        object Success : TakePictureFromCamera()
        data class Failure(val error : String) : TakePictureFromCamera()
        object CapturePhoto : TakePictureFromCamera()
    }

    sealed class DeletePictureFromStorage: ViewResult(){
        object Success : DeletePictureFromStorage()
        data class Failure(val error : String) : DeletePictureFromStorage()
        object DeleteImage : DeletePictureFromStorage()
    }

    sealed class LoadImagesFromInternalStorage: ViewResult(){
        data class Success(val fileName : String, val bitmap: Bitmap) : LoadImagesFromInternalStorage()
        data class Failure(val error : String) : LoadImagesFromInternalStorage()
        object LoadImages : LoadImagesFromInternalStorage()
    }

    sealed class LoadImagesFromExternalStorage: ViewResult(){
        data class Success(val fileName : String, val bitmap: Bitmap) : LoadImagesFromExternalStorage()
        data class Failure(val error : String) : LoadImagesFromExternalStorage()
        object LoadImages : LoadImagesFromExternalStorage()
    }

}
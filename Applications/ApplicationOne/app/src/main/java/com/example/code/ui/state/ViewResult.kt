package com.example.code.ui.state

sealed class ViewResult {

    object InitialState : ViewResult()

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
        object Success : LoadImagesFromInternalStorage()
        data class Failure(val error : String) : LoadImagesFromInternalStorage()
        object LoadImages : LoadImagesFromInternalStorage()
    }

    sealed class SaveImageToInternalStorage: ViewResult(){
        object Success : SaveImageToInternalStorage()
        data class Failure(val error : String) : SaveImageToInternalStorage()
        object LoadImages : SaveImageToInternalStorage()
    }

}
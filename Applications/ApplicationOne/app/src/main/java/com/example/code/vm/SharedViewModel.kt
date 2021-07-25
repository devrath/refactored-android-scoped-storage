package com.example.code.vm

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.code.ui.state.ViewResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class SharedViewModel (application: Application) : AndroidViewModel(application) {

    private val _view = MutableStateFlow<ViewResult>(ViewResult.InitialState)
    val view = _view.asStateFlow()

    var isPrivate = false
    var readPermissionGranted = false
    var writePermissionGranted = false

    var deletedImageUri: Uri? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) {
            exception.localizedMessage?.let{
                _view.emit(ViewResult.AlertMessage(message = it))
            }
        }
    }

    fun storeImageToInternalStorage(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val name : String = UUID.randomUUID().toString()
            val result = ViewResult.LoadImagesFromInternalStorage.Success(fileName = name, bitmap = bitmap)
            _view.emit(result)
        }
    }

    fun storeImageToExternalStorage(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val name : String = UUID.randomUUID().toString()
            val result = ViewResult.LoadImagesFromExternalStorage.Success(fileName = name, bitmap = bitmap)
            _view.emit(result)
        }
    }

    fun displayAlert(message: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val result = ViewResult.AlertMessage(message = message)
            _view.emit(result)
        }
    }


    fun deleteImageFromExternalStorage() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val result = ViewResult.DeletePictureFromStorage.Success
            _view.emit(result)
        }
    }

}
package com.example.code.vm

import android.app.Application
import android.graphics.Bitmap
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

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) {
            exception.localizedMessage?.let{
                _view.emit(ViewResult.ErrorMessage(error = it))
            }
        }
    }

    fun loadImagesFromInternalStorage(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val name : String = UUID.randomUUID().toString()
            val result = ViewResult.LoadImagesFromInternalStorage.Success(fileName = name, bitmap = bitmap)
            _view.emit(result)
        }
    }


}
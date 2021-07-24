package com.example.code.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.code.ui.state.ViewResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel (application: Application) : AndroidViewModel(application) {

    private val _view = MutableStateFlow<ViewResult>(ViewResult.InitialState)
    val view = _view.asStateFlow()


}
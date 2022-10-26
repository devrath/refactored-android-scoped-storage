/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.scopeo

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Application
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentUris
import android.content.IntentSender
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
  private val _images = MutableLiveData<List<Image>>()
  val images: LiveData<List<Image>> get() = _images

  private var contentObserver: ContentObserver? = null

  private var pendingDeleteImage: Image? = null
  private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
  val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete


  fun loadImages() {
    viewModelScope.launch {
      val imageList = queryImages()
      _images.postValue(imageList)

      if (contentObserver == null) {
        contentObserver = getApplication<Application>().contentResolver.registerObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ) {
          loadImages()
        }
      }
    }
  }

  fun deleteImage(image: Image) {
    viewModelScope.launch {
      performDeleteImage(image)
    }
  }

  fun deletePendingImage() {
    pendingDeleteImage?.let { image ->
      pendingDeleteImage = null
      deleteImage(image)
    }
  }

  @TargetApi(Build.VERSION_CODES.Q)
  private suspend fun queryImages(): List<Image> {
    var imageList = mutableListOf<Image>()

    withContext(Dispatchers.IO) {
      val projection = arrayOf(
          MediaStore.Images.Media._ID,
          MediaStore.Images.Media.DISPLAY_NAME,
          MediaStore.Images.Media.DATE_TAKEN
      )

      val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"

      val selectionArgs = arrayOf(
          dateToTimestamp(day = 1, month = 1, year = 2020).toString()
      )

      val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

      getApplication<Application>().contentResolver.query(
          MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
          projection,
          selection,
          selectionArgs,
          sortOrder
      )?.use { cursor ->
        imageList = addImagesFromCursor(cursor)
      }
    }

    return imageList
  }

  @TargetApi(Build.VERSION_CODES.Q)
  private fun addImagesFromCursor(cursor: Cursor): MutableList<Image> {
    val images = mutableListOf<Image>()


    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
    val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
    val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

    while (cursor.moveToNext()) {

      val id = cursor.getLong(idColumn)
      val dateTaken = Date(cursor.getLong(dateTakenColumn))
      val displayName = cursor.getString(displayNameColumn)

      val contentUri = ContentUris.withAppendedId(
          MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
          id
      )

      val image = Image(id, displayName, dateTaken, contentUri)
      images += image

    }
    return images
  }

  private suspend fun performDeleteImage(image: Image) {
    withContext(Dispatchers.IO) {
      try {
        getApplication<Application>().contentResolver.delete(
            image.contentUri,
            "${MediaStore.Images.Media._ID} = ?",
            arrayOf(image.id.toString()
            )
        )
      } catch (securityException: SecurityException) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          val recoverableSecurityException =
              securityException as? RecoverableSecurityException
                  ?: throw securityException
          pendingDeleteImage = image
          _permissionNeededForDelete.postValue(
              recoverableSecurityException.userAction.actionIntent.intentSender
          )
        } else {
          throw securityException
        }
      }
    }
  }

  @Suppress("SameParameterValue")
  @SuppressLint("SimpleDateFormat")
  private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
      SimpleDateFormat("dd.MM.yyyy").let { formatter ->
        formatter.parse("$day.$month.$year")?.time ?: 0
      }


  override fun onCleared() {
    contentObserver?.let {
      getApplication<Application>().contentResolver.unregisterContentObserver(it)
    }
  }
}

/**
 * Extension method to register a [ContentObserver]
 */
private fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
  val contentObserver = object : ContentObserver(Handler()) {
    override fun onChange(selfChange: Boolean) {
      observer(selfChange)
    }
  }
  registerContentObserver(uri, true, contentObserver)
  return contentObserver
}

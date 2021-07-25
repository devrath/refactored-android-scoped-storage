package com.example.code.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import com.example.code.models.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object InternalStorageUtils {


    /**
     * Saving the photo in internal storage
     * @param filename - It is the name of the file we are storing
     * @param bmp - It is the bitmap used to create the file
     * **********************************************************************
     * @return true/false based on if the image is successfully stored or not
     */
     suspend fun savePhotoToInternalStorage(filename: String, bmp: Bitmap, activity: Activity): Boolean {
        // When we are dealing with the storage, It is better to wrap our code with try/catch block since many things might go wrong
        return withContext(Dispatchers.IO) {
            try {
                /* We use the openFileOutput to handle the streams, Bitmap is nothing but a chunk of data
                 * Use is a kotlin extension function we use for file handling, it helps us to close the stream after being used */
                activity.openFileOutput("$filename.jpg", AppCompatActivity.MODE_PRIVATE).use { stream ->
                    // Returns true if the write is successful else it throws exception
                    if(!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        // Returns an exception in case of a failure
                        throw IOException("Couldn't save bitmap.")
                    }
                }
                true
            } catch(e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }


    /**
     * This function will load the list of photos from a internal storage
     * The function is a suspend function since it takes lot of time to load
     * @return List of files retrieved from the internal storage
     */
     suspend fun loadPhotosFromInternalStorage(activity: Activity): List<InternalStoragePhoto> {
        /* Since it will take lot of time to load the photos from the internal storage,
         * lets use a IO dispatcher */
        return withContext(Dispatchers.IO) {
            // filesDir : -> Refers to the root directory of our internal storage
            // Here we get reference list to all files in root folder

            activity.let { activity ->
                val files = activity.filesDir.listFiles()

                files?.filter { file ->
                    // If the file can be read, It is a file, It ends with .jpg
                    file.canRead() && file.isFile && file.name.endsWith(".jpg")
                }?.map {
                    // For the filtered files -> For each file get the bytes of individual file one by one
                    val bytes = it.readBytes()
                    // Convert the bytes to bitmap
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    // Return the model that comprise of file name and the bitmap
                    InternalStoragePhoto(it.name, bmp)
                } ?: listOf()
            }

        }
    }


    /**
     * This function is used to delete the file from the mentioned file location
     * @param filename -> Name of the file to be deleted
     * @return true/false -> Based on successful and unsuccessful condition
     */
     suspend  fun deletePhotoFromInternalStorage(filename: String,activity: Activity): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                activity.deleteFile(filename)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

}
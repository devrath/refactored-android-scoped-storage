package com.example.code.utils

import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.example.code.models.SharedStoragePhoto
import com.example.code.sdk29AndUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object ExternalStorageUtils {

     suspend fun loadPhotosFromExternalStorage(activity: Activity): List<SharedStoragePhoto> {

        return withContext(Dispatchers.IO) {
            activity.let {
                // The URI : This is the medium which we want to query
                val imageCollection = deviceImageCollectionExternalVolume()

                // Projection : Just which of the end results we want to return, Like what we are interested in and what is the meta data
                val projection = arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.WIDTH,
                        MediaStore.Images.Media.HEIGHT,
                )

                // COLLECTION : The photos
                val photos = mutableListOf<SharedStoragePhoto>()

                // We can use a SQL based type of query to define the type of data we are interested in adding to the collection
                it.contentResolver.query(imageCollection,
                        projection,
                        null,
                        null,
                        "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
                )?.use { cursor ->
                    // CURSOR:-> It is used to iterate over the result where we can access the columns of specific fields

                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                    val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

                    // As long as there are new items in our result set, we will move to next entry
                    while(cursor.moveToNext()) {
                        // For each entry -> Handle in the while loop
                        val id = cursor.getLong(idColumn)
                        val displayName = cursor.getString(displayNameColumn)
                        val width = cursor.getInt(widthColumn)
                        val height = cursor.getInt(heightColumn)
                        val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        photos.add(SharedStoragePhoto(id, displayName, width, height, contentUri))
                    }
                    photos.toList()
                } ?: listOf()
            }
        }

    }




    /**
     * Saving the photo in external storage
     * @param filename - It is the name of the file we are storing
     * @param bmp - It is the bitmap used to create the file
     * **********************************************************************
     * @return true/false based on if the image is successfully stored or not
     */
     suspend fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap, activity: Activity): Boolean {

        return withContext(Dispatchers.IO){
            /* ******************************************************************
            * MEDIA STORE:-> It is a huge data base with all media files and corresponding meta data
            * USAGE of media store:-> With the media store we can get the collection of images and add image to that collection
            * URI:-> Address where the image is stored
            * USAGE of uri:-> Getting the uri is different below API 29 and different above 29 remember
            * ******************************************************************
            * */
            val imageCollection = deviceImageCollection()

            /**
             * We not just want to save the bitmap but also the meta data along with it
             * These data can be used by us and other apps -> For that we use content values
             * CONTENT VALUES: They are similar to bundles where we can
             * **/
            /**
             * We not just want to save the bitmap but also the meta data along with it
             * These data can be used by us and other apps -> For that we use content values
             * CONTENT VALUES: They are similar to bundles where we can
             * **/
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bmp.width)
                put(MediaStore.Images.Media.HEIGHT, bmp.height)
            }

            try {

                activity.let {
                    // We use content resolver to insert all the media store entries or read them.

                    // Insert at a particular URI and at that Uri we have to add all the content values
                    it.contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                        /* We use the openFileOutput to handle the streams, Bitmap is nothing but a chunk of data
                         * Use is a kotlin extension function we use for file handling, it helps us to close the stream after being used */
                        activity?.contentResolver?.openOutputStream(uri).use { outputStream ->
                            // Returns true if the write is successful else it throws exception
                            if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                                // Returns an exception in case of a failure
                                throw IOException("Couldn't save bitmap")
                            }

                        }
                    } ?: throw IOException("Couldn't create MediaStore entry")
                }

                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

    /**
     * Based on the device API level - Retrieve the URI
     */
    private fun deviceImageCollection(): Uri {
        return sdk29AndUp { // For the SDK 29 and above use the media store to get the URI
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: // For the SDK below the 29 use the external URI
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    /**
     * Based on the device API level - Retrieve the URI
     * VOLUME_EXTERNAL - All the images from all external source
     */
    private fun deviceImageCollectionExternalVolume(): Uri {
        return sdk29AndUp { // For the SDK 29 and above use the media store to get the URI
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } ?: // For the SDK below the 29 use the external URI
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }


}
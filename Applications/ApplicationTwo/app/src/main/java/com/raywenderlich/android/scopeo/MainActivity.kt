package com.raywenderlich.android.scopeo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

private const val READ_EXTERNAL_STORAGE_REQUEST = 1

private const val DELETE_PERMISSION_REQUEST = 2

class MainActivity : AppCompatActivity() {

  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    // Switch to AppTheme for displaying the activity
    setTheme(R.style.AppTheme)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val galleryAdapter = GalleryAdapter { image ->
      deleteImage(image)
    }

    imageGallery.also { view ->
      view.layoutManager = GridLayoutManager(this, 3)
      view.adapter = galleryAdapter
    }

    viewModel.images.observe(this, Observer<List<Image>> { images ->
      galleryAdapter.submitList(images)
    })

    viewModel.permissionNeededForDelete.observe(this, Observer { intentSender ->
      intentSender?.let {
        startIntentSenderForResult(
            intentSender,
            DELETE_PERMISSION_REQUEST,
            null,
            0,
            0,
            0,
            null
        )
      }
    })

    openAlbumButton.setOnClickListener { openMediaStore() }
    grantPermissionButton.setOnClickListener { openMediaStore() }

    if (!haveStoragePermission()) {
      albumContainer.visibility = View.VISIBLE
    } else {
      showImages()
    }
  }

  @SuppressLint("MissingSuperCall")
  override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<String>,
      grantResults: IntArray
  ) {
      when (requestCode) {
      READ_EXTERNAL_STORAGE_REQUEST -> {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          showImages()
        } else {
          // If we weren't granted the permission, check to see if we should show
          // rationale for the permission.
          val showRationale =
              ActivityCompat.shouldShowRequestPermissionRationale(
                  this,
                  Manifest.permission.READ_EXTERNAL_STORAGE
              )
          if (showRationale) {
            showNoAccess()
          } else {
            goToSettings()
          }
        }
        return
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == DELETE_PERMISSION_REQUEST) {
      viewModel.deletePendingImage()
    }
  }

  private fun showImages() {
    viewModel.loadImages()
    albumContainer.visibility = View.GONE
    permissionContainer.visibility = View.GONE
  }

  private fun showNoAccess() {
    albumContainer.visibility = View.GONE
    permissionContainer.visibility = View.VISIBLE
  }

  private fun openMediaStore() {
    if (haveStoragePermission()) {
      showImages()
    } else {
      requestPermission()
    }
  }

  private fun goToSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")).apply {
      addCategory(Intent.CATEGORY_DEFAULT)
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }.also { intent ->
      startActivity(intent)
    }
  }


  private fun haveStoragePermission() =
      ContextCompat.checkSelfPermission(
          this,
          Manifest.permission.READ_EXTERNAL_STORAGE
      ) == PackageManager.PERMISSION_GRANTED


  private fun requestPermission() {
    if (!haveStoragePermission()) {
      val permissions = arrayOf(
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE
      )
      ActivityCompat.requestPermissions(
          this,
          permissions,
          READ_EXTERNAL_STORAGE_REQUEST
      )
    }
  }

  private fun deleteImage(image: Image) {
    MaterialAlertDialogBuilder(this)
        .setTitle(R.string.delete_dialog_title)
        .setMessage(getString(R.string.delete_dialog_message, image.displayName))
        .setPositiveButton(R.string.delete_dialog_positive) { _: DialogInterface, _: Int ->
          viewModel.deleteImage(image)
        }
        .setNegativeButton(R.string.delete_dialog_negative) { dialog: DialogInterface, _: Int ->
          dialog.dismiss()
        }
        .show()
  }

  private inner class GalleryAdapter(val onClick: (Image) -> Unit) :
      ListAdapter<Image, ImageViewHolder>(Image.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val view = layoutInflater.inflate(R.layout.image_layout, parent, false)
      return ImageViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
      val image = getItem(position)
      holder.rootView.tag = image

      Glide.with(holder.imageView)
          .load(image.contentUri)
          .thumbnail(0.33f)
          .centerCrop()
          .into(holder.imageView)
    }
  }
}

private class ImageViewHolder(view: View, onClick: (Image) -> Unit) :
    RecyclerView.ViewHolder(view) {
  val rootView = view
  val imageView: ImageView = view.findViewById(R.id.image)

  init {
    imageView.setOnClickListener {
      val image = rootView.tag as? Image ?: return@setOnClickListener
      onClick(image)
    }
  }
}

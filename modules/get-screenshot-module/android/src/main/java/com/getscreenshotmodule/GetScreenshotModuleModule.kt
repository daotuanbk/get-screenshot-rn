package com.getscreenshotmodule

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.nmd.screenshot.Screenshot
class GetScreenshotModuleModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun multiply(a: Double, b: Double, promise: Promise) {
    promise.resolve(a * b)
  }

  @SuppressLint("Range")
  @ReactMethod
  fun getScreenshot(promise: Promise) {
     val projection = arrayOf(MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA)
        val cursor = reactApplicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Images.Media.DATA + " LIKE ?",
            arrayOf("%Screenshots%"),
            MediaStore.Images.ImageColumns._ID + " DESC"
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndex(MediaStore.Images.ImageColumns._ID))
                val screenshotUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                promise.resolve(screenshotUri.toString())
            } else {
                promise.reject("ERROR", "No screenshot found")
            }
        } ?: promise.reject("ERROR", "Failed to query MediaStore")
  }


  companion object {
    const val NAME = "GetScreenshotModule"
  }
}

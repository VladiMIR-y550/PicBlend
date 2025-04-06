package ua.smartmir.picblend.features.camera.data.saveimage

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.IOException
import javax.inject.Inject

interface ImageRepository {

    suspend fun saveBitmapToGallery(imageData: Bitmap, onPhotoSaved: (Uri) -> Unit)

    class Base @Inject constructor(
        private val contentResolver: ContentResolver
    ) : ImageRepository {

        override suspend fun saveBitmapToGallery(
            imageData: Bitmap,
            onPhotoSaved: (Uri) -> Unit
        ) {
            val filename = "IMG_${System.currentTimeMillis()}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PicBlend")
            }

            val contentResolver = contentResolver
            val imageUri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return

            try {
                contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                    imageData.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                onPhotoSaved(imageUri)
            } catch (e: IOException) {
                Log.e("CameraX", e.message.toString())
            }
        }
    }
}
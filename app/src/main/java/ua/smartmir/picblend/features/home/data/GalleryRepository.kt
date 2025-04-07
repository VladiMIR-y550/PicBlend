package ua.smartmir.picblend.features.home.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GalleryRepository {
    suspend fun loadBitmapFromUri(uri: Uri): Bitmap?

    class Base @Inject constructor(
        private val contentResolver: ContentResolver
    ) : GalleryRepository {
        override suspend fun loadBitmapFromUri(uri: Uri): Bitmap? {
            return withContext(Dispatchers.IO) {
                val inputStream = contentResolver.openInputStream(uri)
                try {
                    BitmapFactory.decodeStream(inputStream)
                } catch (e: Exception) {
                    Log.e("TAG_GalleryRepository", "loadBitmapFromUri: ${e.message}")
                    null
                } finally {
                    inputStream?.close()
                }
            }
        }
    }
}
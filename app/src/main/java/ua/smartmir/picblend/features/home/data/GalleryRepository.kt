package ua.smartmir.picblend.features.home.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject

interface GalleryRepository {
    suspend fun loadBitmapFromUri(uri: Uri): Bitmap?

    class Base @Inject constructor(
        private val contentResolver: ContentResolver
    ) : GalleryRepository {
        override suspend fun loadBitmapFromUri(uri: Uri): Bitmap? {
            var stream: InputStream? = null
            return withContext(Dispatchers.IO) {
                try {
                    contentResolver.openInputStream(uri)?.use { input ->
                        stream = input
                        BitmapFactory.decodeStream(input)
                    }
                } catch (e: Exception) {
                    null
                } finally {
                    stream?.close()
                }
            }
        }
    }
}
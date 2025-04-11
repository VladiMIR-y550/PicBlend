package ua.smartmir.picblend.features.remote_images.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface BitmapManager {
    suspend fun bitmapByUrl(url: String): Bitmap?

    class CoilBitmapManager @Inject constructor(
        @ApplicationContext private val context: Context
    ) : BitmapManager {
        override suspend fun bitmapByUrl(url: String): Bitmap? {
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()

            val result = ImageLoader(context).execute(request)
            val drawable = result.drawable
            return (drawable as? BitmapDrawable)?.bitmap
        }
    }
}
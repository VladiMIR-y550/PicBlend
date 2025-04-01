package ua.smartmir.picblend.features.camera.domain

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface FilterProcessor {
    suspend fun applyFilter(image: Bitmap, filter: FilterType): Bitmap
    suspend fun resizeWithAspectRatio(image: Bitmap, targetSize: Int): Bitmap

    class PhotoFilterProcessor @Inject constructor() : FilterProcessor {

        override suspend fun applyFilter(
            image: Bitmap,
            filter: FilterType
        ): Bitmap {
            return withContext(Dispatchers.Default) {
                with(image) {
                    config?.let {
                        createBitmap(width, height, it).also { filteredBitmap ->
                            val canvas = Canvas(filteredBitmap)
                            val paint = Paint().apply {
                                colorFilter = ColorMatrixColorFilter(filter.colorMatrix())
                            }
                            canvas.drawBitmap(this, 0f, 0f, paint)
                        }
                    } ?: image
                }
            }
        }

        override suspend fun resizeWithAspectRatio(
            image: Bitmap,
            targetSize: Int
        ): Bitmap {
            return withContext(Dispatchers.Default) {
                val aspectRatio = image.width.toFloat() / image.height.toFloat()
                val newWidth: Int
                val newHeight: Int
                if (image.width > image.height) {
                    newWidth = targetSize
                    newHeight = (targetSize / aspectRatio).toInt()
                } else {
                    newHeight = targetSize
                    newWidth = (targetSize * aspectRatio).toInt()
                }
                image.scale(newWidth, newHeight)
            }
        }
    }
}
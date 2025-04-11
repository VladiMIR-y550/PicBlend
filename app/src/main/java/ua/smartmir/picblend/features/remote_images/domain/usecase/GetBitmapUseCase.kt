package ua.smartmir.picblend.features.remote_images.domain.usecase

import android.graphics.Bitmap
import ua.smartmir.picblend.features.remote_images.domain.BitmapManager
import javax.inject.Inject

class GetBitmapUseCase @Inject constructor(
    private val bitmapManager: BitmapManager
) {
    suspend fun getBitmap(url: String): Bitmap? = bitmapManager.bitmapByUrl(url)
}
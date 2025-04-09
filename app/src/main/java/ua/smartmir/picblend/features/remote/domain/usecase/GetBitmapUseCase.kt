package ua.smartmir.picblend.features.remote.domain.usecase

import android.graphics.Bitmap
import ua.smartmir.picblend.features.remote.domain.BitmapManager
import javax.inject.Inject

class GetBitmapUseCase @Inject constructor(
    private val bitmapManager: BitmapManager
) {
    suspend fun getBitmap(url: String): Bitmap? = bitmapManager.bitmapByUrl(url)
}
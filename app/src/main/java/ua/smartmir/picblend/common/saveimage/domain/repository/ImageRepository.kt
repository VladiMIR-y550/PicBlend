package ua.smartmir.picblend.common.saveimage.domain.repository

import android.graphics.Bitmap
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult

interface ImageRepository {
    suspend fun saveBitmap(
        image: Bitmap,
        fileName: String,
        onPhotoSaved: (SavedImageResult) -> Unit
    )
}
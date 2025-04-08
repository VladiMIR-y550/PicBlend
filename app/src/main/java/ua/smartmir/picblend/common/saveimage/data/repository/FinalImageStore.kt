package ua.smartmir.picblend.common.saveimage.data.repository

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult.ErrorImageInfo
import ua.smartmir.picblend.common.saveimage.data.model.SavedImageResult.SuccessImageInfo
import ua.smartmir.picblend.common.saveimage.domain.repository.ImageRepository
import ua.smartmir.picblend.core.getCacheImageUri
import java.io.File
import javax.inject.Inject

class FinalImageStore @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageRepository {
    override suspend fun saveBitmapToGallery(
        image: Bitmap,
        fileName: String,
        onPhotoSaved: (SavedImageResult) -> Unit
    ) {
        try {
            val cachePath = File(context.cacheDir, "shared_images")
            cachePath.mkdirs()

            val finalName = "$fileName.png"
            val file = File(cachePath, finalName)
            file.outputStream().use { outputStream ->
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }

            val uri = context.getCacheImageUri(file)
            onPhotoSaved(SuccessImageInfo(name = finalName, uri = uri))
        } catch (e: Exception) {
            onPhotoSaved(ErrorImageInfo(error = e, errorMessage = "FinalImageStore: ${e.message}"))
        }
    }
}
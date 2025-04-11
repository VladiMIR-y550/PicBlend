package ua.smartmir.picblend.features.saveimage.data.repository

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.ErrorImageInfo
import ua.smartmir.picblend.features.saveimage.data.model.SavedImageResult.SuccessImageInfo
import ua.smartmir.picblend.features.saveimage.domain.repository.ImageRepository
import ua.smartmir.picblend.core.getCacheImageUri
import java.io.File
import javax.inject.Inject

class CacheDirStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageRepository {
    companion object {
        private const val CACHE_DIRECTORY = "shared_images"
    }
    override suspend fun saveBitmap(
        image: Bitmap,
        fileName: String,
        onPhotoSaved: (SavedImageResult) -> Unit
    ) {
        try {
            val cachePath = File(context.cacheDir, CACHE_DIRECTORY)
            clearCacheDirectory(cachePath)

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

    fun clearCacheDirectory(directory: File) {
        if (directory.exists()) {
            directory.listFiles()?.forEach { it.delete() }
        } else {
            directory.mkdirs()
        }
    }
}
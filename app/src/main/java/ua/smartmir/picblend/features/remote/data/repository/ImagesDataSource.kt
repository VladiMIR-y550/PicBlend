package ua.smartmir.picblend.features.remote.data.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ua.smartmir.picblend.R
import ua.smartmir.picblend.features.remote.data.api.UnsplashApi
import ua.smartmir.picblend.features.remote.data.model.UnsplashPhotoDto
import java.io.IOException
import javax.inject.Inject

interface ImagesDataSource {
    suspend fun getImages(page: Int): Result<List<UnsplashPhotoDto>>
    suspend fun safeTrackDownload(downloadLocation: String)

    class UnsplashImagesSource @Inject constructor(
        private val unsplashApi: UnsplashApi,
        @ApplicationContext private val context: Context
    ) : ImagesDataSource {
        override suspend fun getImages(page: Int): Result<List<UnsplashPhotoDto>> {
            return withContext(Dispatchers.IO) {
                try {
                    val photos = unsplashApi.getPhotos(page)
                    Result.success(photos)
                } catch (e: HttpException) {
                    when (e.code()) {
                        400 -> Result.failure(Exception(context.getString(R.string.invalid_request)))
                        401 -> Result.failure(Exception(context.getString(R.string.unauthorized)))
                        403 -> Result.failure(Exception(context.getString(R.string.rate_limit_exceeded)))
                        500 -> Result.failure(Exception(context.getString(R.string.server_error)))
                        else -> Result.failure(Exception(context.getString(R.string.error, "${e.code()}")))
                    }
                } catch (e: IOException) {
                    Result.failure(Exception(context.getString(R.string.no_internet_connection)))
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }

        override suspend fun safeTrackDownload(downloadLocation: String) {
            Log.e("TAG_Unsplash", "trackDownload: $downloadLocation")
            try {
                val response = unsplashApi.trackDownload(downloadLocation)
                if (!response.isSuccessful) {
                    Log.w("TAG_Unsplash", "Track failed: ${response.code()} - $downloadLocation")
                }
            } catch (e: Exception) {
                Log.e("TAG_Unsplash", "Track error: $downloadLocation", e)
            }
        }
    }
}
package ua.smartmir.picblend.di.remote_images.network

import okhttp3.Interceptor
import okhttp3.Response
import ua.smartmir.picblend.BuildConfig
import javax.inject.Inject

class UnsplashInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val newUrl = originalUrl.newBuilder()
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .header("Authorization", "Client-ID ${BuildConfig.UNSPLASH_ACCESS_KEY}")
            .build()

        return chain.proceed(newRequest)
    }
}
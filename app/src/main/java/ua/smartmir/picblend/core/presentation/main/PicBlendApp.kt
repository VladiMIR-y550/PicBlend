package ua.smartmir.picblend.core.presentation.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ua.smartmir.picblend.core.ConnectionService
import javax.inject.Inject

@HiltAndroidApp
class PicBlendApp : Application() {

    @Inject
    lateinit var connectionService: ConnectionService
    override fun onTerminate() {
        super.onTerminate()
        connectionService.cleanup()
    }
}
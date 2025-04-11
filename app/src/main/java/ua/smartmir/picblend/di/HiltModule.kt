package ua.smartmir.picblend.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.smartmir.picblend.di.camera.CameraModule
import ua.smartmir.picblend.di.network.ApiModule

@Module(
    includes = [
        RepositoryModule::class,
        DomainModule::class,
        ApiModule::class,
        CameraModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
open class HiltModule
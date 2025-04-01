package ua.smartmir.picblend.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        RepositoryModule::class,
        ImageModule::class,
        CameraModule::class,
        UseCaseModule::class
    ]
)
@InstallIn(SingletonComponent::class)
open class HiltModule
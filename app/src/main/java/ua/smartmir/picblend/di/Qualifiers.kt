package ua.smartmir.picblend.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope()

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CameraApplyFilter

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CameraFilters

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EditorFilters

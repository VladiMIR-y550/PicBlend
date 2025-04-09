package ua.smartmir.picblend.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplyFilter

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Camera

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Editor

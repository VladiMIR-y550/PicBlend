package ua.smartmir.picblend.di

import jakarta.inject.Qualifier

@Qualifier
//@Retention(AnnotationRetention.BINARY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope()
